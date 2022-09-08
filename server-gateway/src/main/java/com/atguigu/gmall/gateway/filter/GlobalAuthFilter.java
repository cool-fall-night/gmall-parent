package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.gateway.properties.AuthUrlProperties;
import com.atguigu.gmall.model.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局授权过滤器
 *
 * @author 毛伟臣
 * 2022/9/7
 * 19:10
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter {

    @Autowired
    private AuthUrlProperties authUrlProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取请求路径,开始拦截
        String path = exchange.getRequest().getURI().getPath();
        String uri = exchange.getRequest().getURI().toString();

//        log.info("{} 请求开始", path);
        //1、无需登录就可访问的资源
        for (String url : authUrlProperties.getNoAuthUrl()) {
            //遍历放行列表
            boolean match = matcher.match(url, path);
            if (match) {
                //匹配成功,拦截放行
                return chain.filter(exchange);
            }
        }

        //2、禁止访问的资源
        for (String url : authUrlProperties.getDenyAuthUrl()) {
            //遍历放行列表
            boolean match = matcher.match(url, path);
            if (match) {
                //匹配成功,直接响应json
                Result<String> build = Result.build("", ResultCodeEnum.PERMISSION);
                return responseResult(build, exchange);
            }
        }

        //3、需要登录才可访问的资源
        for (String url : authUrlProperties.getLoginAuthUrl()) {
            //遍历放行列表
            boolean match = matcher.match(url, path);

            if (match) {
                //匹配成功，进行登录验证（通过请求中的token进行判定）
                String tokenValue = getTokenValue(exchange);
                UserInfo info = getUserInfo(tokenValue);

                if (!StringUtils.isEmpty(info)) {
                    ServerWebExchange webExchange = userIdTransport(info, exchange);
                    //redis中有此对象，放行
                    return chain.filter(webExchange);
                } else {
                    //redis中无此对象，重定向到指定页面
                    return redirectToConsumerPage(authUrlProperties.getLoginPage() + "?originUrl=" + uri, exchange);
                }
            }
        }
        //4、无需拦截的一般请求，直接放行
        String tokenValue = getTokenValue(exchange);
        UserInfo info = getUserInfo(tokenValue);
        if (info != null) {
            exchange = userIdTransport(info, exchange);
        }else {
            if (!StringUtils.isEmpty(tokenValue)) {
                return redirectToConsumerPage(authUrlProperties.getLoginPage() + "?originUrl=" + uri, exchange);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> responseResult(Result<String> build, ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        String jsonStr = Jsons.toJsonStr(build);
        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonStr.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

    private ServerWebExchange userIdTransport(UserInfo info, ServerWebExchange exchange) {

        if (info != null) {
            ServerHttpRequest request = exchange.getRequest();

            String userTempId = getUserTempId(exchange);

            ServerHttpRequest newRequest = request.mutate()
                    .header(RedisConst.USERID_HEADER, info.getId().toString())
                    .header(RedisConst.USERTEMPID_HEADER,userTempId)
                    .build();
            return exchange.mutate().request(newRequest).response(exchange.getResponse()).build();
        }
        return exchange;
    }

    private String getUserTempId(ServerWebExchange exchange) {

        String userTempId = exchange.getRequest().getHeaders().getFirst(RedisConst.USERTEMPID_HEADER);
        if (StringUtils.isEmpty(userTempId)){
            HttpCookie cookie = exchange.getRequest().getCookies().getFirst(RedisConst.USERTEMPID_HEADER);
            if (cookie != null){
                userTempId = cookie.getValue();
            }
        }
        return userTempId;
    }

    private Mono<Void> redirectToConsumerPage(String location, ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().add(HttpHeaders.LOCATION, location);
        //清除久的错误的Cookie
        ResponseCookie tokenCookie = ResponseCookie.from("token", "777").path("/").maxAge(0).domain(".gmall.com").build();
        ResponseCookie userInfoCookie = ResponseCookie.from("userInfo", "777").path("/").maxAge(0).domain(".gmall.com").build();
        response.getCookies().set("token", tokenCookie);
        response.getCookies().set("userInfo", userInfoCookie);

        //响应结束
        return response.setComplete();
    }

    private UserInfo getUserInfo(String tokenValue) {

        String jsons = redisTemplate.opsForValue().get(RedisConst.USER_INFO_PREFIX + tokenValue);
        if (!StringUtils.isEmpty(jsons)) {
            return Jsons.toObj(jsons, UserInfo.class);
        }
        return null;

    }

    private String getTokenValue(ServerWebExchange exchange) {

        String tokenValue = null;
        //若Cookie中携带token
        HttpCookie tokenInCookie = exchange.getRequest().getCookies().getFirst("token");
        if (!StringUtils.isEmpty(tokenInCookie)) {
            tokenValue = tokenInCookie.getValue();
        }
        //若请求头中携带token
        String tokenInHeader = exchange.getRequest().getHeaders().getFirst("token");
        if (!StringUtils.isEmpty(tokenInHeader)) {
            tokenValue = tokenInHeader;
        }
        return tokenValue;
    }
}

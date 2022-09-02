package com.atguigu.starter.cache.aspect;

import com.atguigu.starter.cache.constant.RedisConst;
import com.atguigu.starter.cache.service.CacheOpsService;
import com.atguigu.starter.cache.annotation.GmallCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author 毛伟臣
 * 2022/9/1
 * 20:46
 * @version 1.0
 * @since JDK1.8
 */

@Aspect     //声明为切面
@Component
public class CacheAspect {

    @Autowired
    private CacheOpsService cacheOpsService;

    //创建表达式解析器（线程安全）
    SpelExpressionParser parser = new SpelExpressionParser();
    TemplateParserContext context = new TemplateParserContext();

    @Around("@annotation(com.atguigu.starter.cache.annotation.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result;

        //拼接商品的缓存Key值
        String cacheKey = determineCacheKey(joinPoint);

        //先查缓存
        Type returnType = getMethodGenericReturnType(joinPoint);

        Object cacheData = cacheOpsService.getCacheData(cacheKey, returnType);


        //缓存判定
        if (cacheData == null) {
            // 缓存中不存在，准备回源
            //先问布隆过滤器
            String bloomName = determineBloomName(joinPoint);
            if (!StringUtils.isEmpty(bloomName)) {
                //布隆名不为空时，则开启布隆
                Object bloomValue = determineBloomValue(joinPoint);
                Boolean contains = cacheOpsService.bloomContains(bloomName,bloomValue);
                if (!contains) {
                    //布隆中不存在，判定为疑似恶意攻击
                    return null;
                }
            }

            //布隆中存在，可能存在，加锁回源
            Boolean lock = false;
            String lockName = "";
            try {
                lockName = determineLockName(joinPoint);
                lock = cacheOpsService.tryLock(lockName);
                if (lock) {
                    //抢到锁，推进调用方法
                    result = joinPoint.proceed(joinPoint.getArgs());
                    //方法调用成功，重新缓存，返回结果
                    Long dataTtl = determineDataTtl(joinPoint);
                    cacheOpsService.saveData(cacheKey, result, dataTtl);
                    return result;
                } else {
                    //未抢到锁，睡眠1s，从缓存中查询
                    Thread.sleep(1000L);
                    return cacheOpsService.getCacheData(cacheKey, returnType);
                }
            } finally {
                if (lock) {
                    //若加锁成功，则调用解锁
                    cacheOpsService.unLock(lockName);
                }
            }
        }
        //若缓存中有，返回结果
        return cacheData;
    }



    /**
     * 精确获得方法的返回值类型
     */
    private Type getMethodGenericReturnType(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getGenericReturnType();
    }

    /**
     * 获取注解中的参数
     */
    private String determineCacheKey(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        String expression = cacheAnnotation.cacheKey();

        return evaluationExpression(expression, joinPoint, String.class);
    }

    private String determineBloomName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);

        return cacheAnnotation.bloomName();
    }

    private Object determineBloomValue(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        String expression = cacheAnnotation.bloomValue();

        return evaluationExpression(expression, joinPoint, Object.class);
    }

    private String determineLockName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        String lockName = cacheAnnotation.lockName();
        if (StringUtils.isEmpty(lockName)){
            return RedisConst.LOCK_PREFICX + method.getName();
        }
        return evaluationExpression(lockName, joinPoint, String.class);
    }

    private Long determineDataTtl(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        return cacheAnnotation.dataTtl();
    }

    private <T> T evaluationExpression(String expression,
                                       ProceedingJoinPoint joinPoint,
                                       Class<T> clz) {

        //得到表达式
        Expression exp = parser.parseExpression(expression, context);
        //标准的上下文
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        //取出参数绑定上下文
        Object[] args = joinPoint.getArgs();
        evaluationContext.setVariable("params", args);
        //得到表达式的值

        return exp.getValue(evaluationContext, clz);
    }

    /* 备注
        //1、获取将要执行的目标方法的签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //2、获取调用目标方法时传递的所有参数
        Object[] args = joinPoint.getArgs();

        //3、放行目标方法
        Method method = signature.getMethod();
        //前置通知
        Object result = null;
        try {
            result = method.invoke(joinPoint.getTarget(), args);
            //返回通知
        } catch (Exception e) {
            throw new RuntimeException(e);
            //异常通知
        } finally {
            //后置通知
        }
        return result;
    */

}

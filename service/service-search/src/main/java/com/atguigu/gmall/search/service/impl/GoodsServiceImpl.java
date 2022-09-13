package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.list.*;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.GoodsService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/5
 * 23:57
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    @Override
    public void savaGoods(Goods goods) {

        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public SearchResponseVo search(SearchParam searchParam) {

        Query query = buildQueryDsl(searchParam);

        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));
        return buildSearchResponseResult(goods, searchParam);
    }

    @Override
    public void increaseHotScore(Long skuId, Long score) {

        //更新热度分
        Goods goods = goodsRepository.findById(skuId).get();
        goods.setHotScore(score);
        //更新Es
        goodsRepository.save(goods);

    }

    /**
     * 根据查询结果构建返回值封装对象
     * @param goods
     * @param searchParam
     * @return
     */
    private SearchResponseVo buildSearchResponseResult(SearchHits<Goods> goods, SearchParam searchParam) {

        SearchResponseVo vo = new SearchResponseVo();

        //1、检索所需的所有参数
        vo.setSearchParam(searchParam);

        //2、品牌面包屑
        if (!StringUtils.isEmpty(searchParam.getTrademark())) {
            vo.setTrademarkParam("品牌：" + searchParam.getTrademark().split(":")[1]);
        }

        //3、属性面包屑  集合：元素为prop对象，属性包含attrId、attrName、attrValue
        if (searchParam.getProps() != null && searchParam.getProps().length > 0) {
            List<SearchAttr> propsParamList = new ArrayList<>();
            for (String prop : searchParam.getProps()){
                String[] split = prop.split(":");
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(Long.parseLong(split[0]));
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                propsParamList.add(searchAttr);
            }
            vo.setPropsParamList(propsParamList);
        }

        //所有品牌列表，需ES聚合分析
        List<TrademarkVo> trademarkVos =  buildTrademarkList(goods);
        vo.setTrademarkList(trademarkVos);

        //所有属性列表，需ES聚合分析
        List<AttrVo> attrsList = buildAttrList(goods);
        vo.setAttrsList(attrsList);

        //6、页面排序信息
        if (!StringUtils.isEmpty(searchParam.getOrder())) {
            String order = searchParam.getOrder();
            OrderMapVo mapVo = new OrderMapVo();
            mapVo.setType(order.split(":")[0]);
            mapVo.setSort(order.split(":")[1]);
            vo.setOrderMap(mapVo);
        }

        //7、商品信息，集合：元素为goods 属性包含id、defaultImg、price、title、
        ArrayList<Goods> goodsList = new ArrayList<>();
        List<SearchHit<Goods>> hits = goods.getSearchHits();
        for (SearchHit<Goods> hit : hits){
            //命中的商品记录
            Goods content = hit.getContent();
            if (!StringUtils.isEmpty(searchParam.getKeyword())) {
                //关键字高亮显示
                String title = hit.getHighlightField("title").get(0);
                content.setTitle(title);
            }
            goodsList.add(content);
        }
        vo.setGoodsList(goodsList);
        //8、分页信息
        vo.setPageNo(searchParam.getPageNo());
        //总词条数
        long totalHits = goods.getTotalHits();
        long ps = totalHits%RedisConst.SEARCH_PAGE_SIZE==0?totalHits/RedisConst.SEARCH_PAGE_SIZE:(totalHits/RedisConst.SEARCH_PAGE_SIZE + 1);
        vo.setTotalPages(new Integer(ps+""));
        //用于拼接的旧链接
        vo.setUrlParam(makeUrlParam(searchParam));

        return vo;
    }

    private List<TrademarkVo> buildTrademarkList(SearchHits<Goods> goods) {

        List<TrademarkVo> trademarkVos = new ArrayList<>();
        //拿到tmIdAgg聚合
        ParsedLongTerms tmIdAgg = goods.getAggregations().get("tmIdAgg");
        for (Terms.Bucket bucket : tmIdAgg.getBuckets()){
            TrademarkVo trademarkVo = new TrademarkVo();
            //获取品牌id
            long tmId = bucket.getKeyAsNumber().longValue();
            trademarkVo.setTmId(tmId);
            //获取品牌名
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmName(tmName);
            //获取品牌logo
            ParsedStringTerms tmLogoAgg = bucket.getAggregations().get("tmLogoAgg");
            String tmLogo = tmLogoAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmLogoUrl(tmLogo);
            trademarkVos.add(trademarkVo);
        }
        return trademarkVos;
    }

    private List<AttrVo> buildAttrList(SearchHits<Goods> goods) {

        List<AttrVo> attrVos = new ArrayList<>();
        //拿到整个属性的聚合结果
        ParsedNested attrAgg = goods.getAggregations().get("attrAgg");
        //拿到属性ID的聚合结果
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        //遍历所有属性Id
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()){
            AttrVo attrVo = new AttrVo();
            //属性id
            long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);
            //属性名
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            List<String> attrValues = new ArrayList<>();
            //遍历属性值
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
            for (Terms.Bucket valueBucket : attrValueAgg.getBuckets()){
                String value = valueBucket.getKeyAsString();
                attrValues.add(value);
            }
            attrVo.setAttrValueList(attrValues);

            attrVos.add(attrVo);
        }
        return attrVos;
    }

    /**
     * 生成用于拼接的旧链接
     * @param searchParam
     */
    private String makeUrlParam(SearchParam searchParam) {

        StringBuilder builder = new StringBuilder("list.html?");
        if (searchParam.getCategory1Id() != null) {
            builder.append("&category1Id=").append(searchParam.getCategory1Id());
        }
        if (searchParam.getCategory2Id() != null) {
            builder.append("&category2Id=").append(searchParam.getCategory2Id());
        }
        if (searchParam.getCategory3Id() != null) {
            builder.append("&category3Id=").append(searchParam.getCategory3Id());
        }
        if (!StringUtils.isEmpty(searchParam.getTrademark())){
            builder.append("&trademark=").append(searchParam.getTrademark());
        }
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            builder.append("&keyword=").append(searchParam.getKeyword());
        }
        if (searchParam.getProps() != null && searchParam.getProps().length > 0) {
            for (String prop : searchParam.getProps()) {
                builder.append("&props=").append(prop);
            }
        }

        return builder.toString();


    }

    /**
     * 根据前端传来的请求参数构建搜索条件
     *
     * @param searchParam
     * @return
     */
    private Query buildQueryDsl(SearchParam searchParam) {
        //准备bool条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //准备must条件
        //前端传来分类信息
        if (searchParam.getCategory1Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category1Id", searchParam.getCategory1Id()));
        }
        if (searchParam.getCategory2Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category2Id", searchParam.getCategory2Id()));
        }
        if (searchParam.getCategory3Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category3Id", searchParam.getCategory3Id()));
        }
        //前端传来品牌信息
        if (!StringUtils.isEmpty(searchParam.getTrademark())) {
            //4:小米
            long tmId = Long.parseLong(searchParam.getTrademark().split(":")[0]);
            boolQuery.must(QueryBuilders.termQuery("tmId", tmId));
        }
        //前端传来关键字
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("title", searchParam.getKeyword()));
        }
        //前端传来属性信息，个数不一定String[] props;
        String[] props = searchParam.getProps();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                //4:128GB:机身存储
                String[] split = prop.split(":");
                long attrId = Long.parseLong(split[0]);
                String attrValue = split[1];
                BoolQueryBuilder nestedBool = QueryBuilders.boolQuery();
                nestedBool.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBool.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBool, ScoreMode.None);
                boolQuery.must(nestedQuery);
            }
        }

        //=====================检索条件结束===========================================================

        //准备原生检索条件（原生的dsl）
        NativeSearchQuery query = new NativeSearchQuery(boolQuery);


        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            //关键字高亮显示
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title")
                    .preTags("<span style='color:red'>").postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            query.setHighlightQuery(highlightQuery);
        }

        //前端传来排序信息 order=2:asc
        if (!StringUtils.isEmpty(searchParam.getOrder())) {
            String[] split = searchParam.getOrder().split(":");
            //TODO 当排序规则超过2个时，转换为switch-case
            String prop = split[0].equals("1") ? "hotScore" : "price";          // 1：综合排序/热点  2：价格
            Sort sort = Sort.by(prop);
            if (RedisConst.SKU_DEFAULT_SORT.equals(split[1])){
                sort = sort.ascending();
            }else{
                sort = sort.descending();
            }
            query.addSort(sort);
        }
        //前端传来页码
        PageRequest request = PageRequest.of(searchParam.getPageNo()-1, RedisConst.SEARCH_PAGE_SIZE);
        query.setPageable(request);

        //=========聚合分析上面DSL检索到的所有商品涉及了多少种品牌和多少种平台属性
        //3、品牌聚合 - 品牌聚合分析条件
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").size(1000);
        //3.1 品牌聚合 - 品牌名子聚合
        TermsAggregationBuilder tmNameAgg = AggregationBuilders.terms("tmNameAgg").field("tmName").size(1);
        //3.2 品牌聚合 - 品牌logo子聚合
        TermsAggregationBuilder tmLogoAgg = AggregationBuilders.terms("tmLogoAgg").field("tmLogoUrl").size(1);
        //品牌id聚合条件拼装完成
        tmIdAgg.subAggregation(tmNameAgg);
        tmIdAgg.subAggregation(tmLogoAgg);
        query.addAggregation(tmIdAgg);

        //4、属性聚合
        //4.1 属性的整个嵌入式聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        //4.2 attrid 聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        //4.3 attrname 聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1);
        //4.4 attrvalue 聚合
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100);
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        attrAgg.subAggregation(attrIdAgg);

        //添加整个属性的聚合条件
        query.addAggregation(attrAgg);
        return query;
    }

}

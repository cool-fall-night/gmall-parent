package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.model.list.*;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.GoodsService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
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

        //TODO 所有品牌列表，需ES聚合分析
        vo.setTrademarkList(new ArrayList());

        //TODO 所有属性列表，需ES聚合分析
        vo.setAttrsList(new ArrayList());

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
        long ps = totalHits%8==0?totalHits/8:(totalHits/8 + 1);
        vo.setTotalPages(new Integer(ps+""));
        //用于拼接的旧链接
        vo.setUrlParam(makeUrlParam(searchParam));

        return vo;
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
            if (split[1].equals("asc")){
                sort = sort.ascending();
            }else{
                sort = sort.descending();
            }
            query.addSort(sort);
        }
        //前端传来页码
        PageRequest request = PageRequest.of(searchParam.getPageNo()-1, 8);
        query.setPageable(request);

        return query;
    }

}

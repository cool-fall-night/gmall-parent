package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 毛伟臣
 * 2022/9/3
 * 23:25
 * @version 1.0
 * @since JDK1.8
 */

@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}

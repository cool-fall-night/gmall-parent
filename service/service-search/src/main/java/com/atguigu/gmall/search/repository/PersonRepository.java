package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.bean.Person;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/3
 * 22:26
 * @version 1.0
 * @since JDK1.8
 */

@Repository
public interface PersonRepository extends CrudRepository<Person,Long> {

    List<Person> findAllByAddressLike(String address);

    @Query("{\n" +
            "    \"match\": {\n" +
            "      \"address\": \"?0\"\n" +
            "    }\n" +
            "  }")
    List<Person> aaa(String add);
}

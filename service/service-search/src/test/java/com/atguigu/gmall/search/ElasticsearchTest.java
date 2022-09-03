package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.Optional;

/**
 * @author 毛伟臣
 * 2022/9/3
 * 22:32
 * @version 1.0
 * @since JDK1.8
 */

@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ElasticsearchRestTemplate restTemplate;

    @Test
    void saveTest(){

        Person person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("德华");
        person1.setLastName("刘");
        person1.setAge(55);
        person1.setAddress("北京市朝阳区");
        Person person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("杰伦");
        person2.setLastName("周");
        person2.setAge(40);
        person2.setAddress("北京市昌平区");
        Person person3 = new Person();
        person3.setId(3L);
        person3.setFirstName("成");
        person3.setLastName("龙");
        person3.setAge(60);
        person3.setAddress("北京市海淀区");
        Person person4 = new Person();
        person4.setId(4L);
        person4.setFirstName("劲松");
        person4.setLastName("王");
        person4.setAge(48);
        person4.setAddress("北京市丰台区");
        Person person5 = new Person();
        person5.setId(5L);
        person5.setFirstName("歌");
        person5.setLastName("胡");
        person5.setAge(35);
        person5.setAddress("上海市奉贤区");

        personRepository.save(person1);
        personRepository.save(person2);
        personRepository.save(person3);
        personRepository.save(person4);
        personRepository.save(person5);
    }

    @Test
    public void queryTest(){
//        Optional<Person> byId = personRepository.findById(3L);
//        System.out.println("byId = " + byId);

//        personRepository.findAllByAddressLike("北京").forEach(System.out::println);
        personRepository.aaa("北京市").forEach(System.out::println);



    }

}

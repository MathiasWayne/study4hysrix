package com.sungrow.hystrix_demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HystrixDemoApplicationTests {
    @Autowired
    private RestTemplate restTemplate;
    private String url="http://localhost:56081/order/findOrders?ids=386969231792537600";
    @Test
    public void testRestTemplate(){
        String forObject = restTemplate.getForObject(url, String.class);
        System.out.println(forObject);
    }
    private String url1="http://localhost:56081/order/findOrders?ids={ids}";
    @Test
    public void contextLoads() {
        Map param=new HashMap<>();
        param.put("ids",386969231792537600L);
        String forObject = restTemplate.getForObject(url1, String.class,param);
        System.out.println(forObject);
    }
    private String url2="http://localhost:56081/order/findOrders?ids={ids}";
    @Test
    public void testForEntity() {
        Map param=new HashMap<>();
        param.put("ids","386969231792537600");
        ResponseEntity forEntity = restTemplate.getForEntity(url2, String.class, param);
        System.out.println(forEntity);
    }


    @Test
    public void postForObject(){
        String url="http://localhost:56081/order/findOrders";
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("ids","386969231792537600");
        String s = restTemplate.postForObject(url, request, String.class);
        System.out.println(s);
    }
}

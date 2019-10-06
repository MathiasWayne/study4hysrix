package com.sungrow.hystrix_demo.controller;


import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sungrow.hystrix_demo.command.GetProductInfoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
/**
 * @author:zhangl
 * @date:2019/10/3
 * @description:
 */

@RestController
@RequestMapping("/hystrix")
@DefaultProperties(defaultFallback = "defaultFallback")
public class hytrixController {
    @Autowired
    private RestTemplate restTemplate;


    //超时配置,服务默认调用超时间是1秒
	/*@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
	})*/
    @HystrixCommand(commandProperties = {
		@HystrixProperty(name = "circuitBreaker.enabled", value = "true"),  				//设置熔断
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),	//请求数达到后才计算
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //休眠时间窗,休眠时间达到10秒后开启halfopen状态
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),	//错误率
	})
    @GetMapping("/getMessage/{message}")
    public String getMessage(@PathVariable("message") Integer message){
	    if(message%2==0){
	        return "success";
        }
        String s = restTemplate.getForObject("http:www.baidu.com", String.class);
        return s;
    }
    private String fallback(){
        return "系统异常";
    }
    private String defaultFallback() {
        return "默认提示：太拥挤了, 请稍后再试~~";
    }

   @GetMapping("/getProductInfo")
    public String getProductInfo(@RequestParam("ids") String productId){
       for (String s : productId.split(",")) {
           GetProductInfoCommand getProductInfoCommand=new GetProductInfoCommand(Long.valueOf(s));
           Object execute = getProductInfoCommand.execute();
           System.out.println("从Hystrix缓存中取出数据："+getProductInfoCommand.isResponseFromCache());
       }
       return "success";
   }
}

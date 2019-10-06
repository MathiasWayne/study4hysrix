package com.sungrow.hystrix_demo.command;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:zhangl
 * @date:2019/10/4
 * @description:
 */

public class GetProductInfoCommand extends HystrixCommand {

    private static  RestTemplate restTemplate =new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    private Long productId;
    private static final HystrixCommandKey KEY = HystrixCommandKey.Factory.asKey("GetProductInfoCommand");
    public GetProductInfoCommand(Long productId){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService")).andCommandKey(KEY)
                // 线程池相关配置信息
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        // 设置线程池大小为8
                        .withCoreSize(8)
                        // 设置等待队列大小为10
                        .withMaxQueueSize(10)
                        .withQueueSizeRejectionThreshold(12))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                // 设置降级机制最大并发请求数
                                .withFallbackIsolationSemaphoreMaxConcurrentRequests(15)
                                // 是否允许断路器工作
                                .withCircuitBreakerEnabled(true)
                                // 滑动窗口中，最少有多少个请求，才可能触发断路
                                .withCircuitBreakerRequestVolumeThreshold(20)
                                // 异常比例达到多少，才触发断路，默认50%
                                .withCircuitBreakerErrorThresholdPercentage(40)
                                // 断路后多少时间内直接reject请求，之后进入half-open状态，默认5000ms
                                .withCircuitBreakerSleepWindowInMilliseconds(3000)
                                // 设置超时时间
                                .withExecutionTimeoutInMilliseconds(2000))
        );
         this.productId=productId;
    }
    /**服务调用处理逻辑*/
    @Override
    protected Object run() throws Exception {
        String url="http://localhost:56081/order/findOrders?ids={id}";
        Map param=new HashMap<>();
        param.put("id",productId);
        String result = restTemplate.getForObject(url, String.class,param);
        return result;
    }

    /**
     * 每次请求的结果，都会放在Hystrix绑定的请求上下文上
     *
     * @return cacheKey 缓存key
     */
    @Override
    public String getCacheKey() {
        return "product_info_" + productId;
    }

    /**
     * 将某个商品id的缓存清空
     *
     * @param productId 商品id
     */
    public static void flushCache(Long productId) {
        HystrixRequestCache.getInstance(KEY,
                HystrixConcurrencyStrategyDefault.getInstance()).clear("product_info_" + productId);
    }


    @Override
    protected Object getFallback() {
        System.out.println("系统异常了");
        return String.valueOf("系统异常了");
    }
}

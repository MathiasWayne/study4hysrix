package com.sungrow.hystrix_demo.command;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;

import java.util.Collection;
import java.util.List;

/**
 * @author:zhangl
 * @date:2019/10/5
 * @description:
 *   请求合并参数：
 *      public abstract class HystrixCollapase<BatchReturnType ,ResponseType,RequestArgumentType></>
 *   BatchReturnType:合并后批量请求的返回值类型
 *   ResponseType：单个请求返回的类型
 *   RequestArgumentType：请求参数类型
 *
 *
 */
public class GetProductInfosCollapser extends HystrixCollapser<List<ProductInfo>, ProductInfo, String> {

    //请求参数
    private String id;

    public GetProductInfosCollapser(String id) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("ProductInfo")).
                andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.id = id;
    }

    /**
     * 参数返回
     */
    @Override
    public String getRequestArgument() {
        return id;
    }

    /**
     * 请求合并
     */
    @Override
    protected HystrixCommand<List<ProductInfo>> createCommand(Collection<CollapsedRequest<ProductInfo, String>> collection) {
        List<String> params=new ArrayList<> (collection.size());
        params.addAll(collection.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        System.out.println("请求合并参数"+params);
        return new ProductInfoCommand(params);
    }
    /**
     * 将请求结果返回合并到结果集中
     */
    @Override
    protected void mapResponseToRequests(List<ProductInfo> batchResponse, Collection<CollapsedRequest<ProductInfo, String>> collapsedRequests) {
        int count = 0 ;
        for(CollapsedRequest<ProductInfo,String> collapsedRequest : collapsedRequests){
            //从批响应集合中按顺序取出结果
            ProductInfo productInfo = batchResponse.get(count++);
            //将结果放回原Request的响应体内
            collapsedRequest.setResponse(productInfo);
        }
    }


    private class ProductInfoCommand extends HystrixCommand<List<ProductInfo>> {
        private  RestTemplate restTemplate =new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        private List<String> productIds;
        public ProductInfoCommand(List<String> productIds) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService")).andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfoCommand"))
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
            this.productIds = productIds;
        }

        @Override
        protected List<ProductInfo> run() throws Exception {
            String url="http://localhost:56081/order/findProductInfosCollar?ids={id}";
            Map param=new HashMap<>();
            param.put("id",productIds);
            List<ProductInfo> pooducts = restTemplate.getForObject(url, List.class,param);
            return pooducts ;
        }
        //短路容错
        @Override
        protected List<ProductInfo> getFallback() {
            List fackBack=new ArrayList();
            ProductInfo productInfo=new ProductInfo();
            productInfo.setOrder_id(1L);
            productInfo.setPrice(new BigDecimal(233));
            productInfo.setStatus("success");
            productInfo.setUser_id(2L);
            fackBack.add(productInfo);
            return fackBack;
        }
       //请求合并缓存
        @Override
        protected String getCacheKey() {
            return "productInfo_"+productIds;
        }
    }
}


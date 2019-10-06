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
public class GetProductInfosCollapser extends HystrixCollapser<List<String>,String,String> {
    @Override
    public String getRequestArgument() {
        return null;
    }

    @Override
    protected HystrixCommand<List<String>> createCommand(Collection<CollapsedRequest<String, String>> collection) {
        return null;
    }

    @Override
    protected void mapResponseToRequests(List<String> strings, Collection<CollapsedRequest<String, String>> collection) {

    }

}

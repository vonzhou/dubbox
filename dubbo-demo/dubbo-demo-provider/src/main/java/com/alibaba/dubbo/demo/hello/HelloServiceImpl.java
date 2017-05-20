/**
 * Copyright 1999-2014 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.hello;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * 在服务提供方实现接口：(对服务消费方隐藏实现)
 * @author lishen
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        boolean isConsumerSide = RpcContext.getContext().isConsumerSide(); // 本端是否为消费端，这里会返回true
        String serverIP = RpcContext.getContext().getRemoteHost(); // 获取最后一次调用的提供方IP地址
        String application = RpcContext.getContext().getUrl().getParameter("application"); // 获取当前服务配置信息，所有配置信息都将转换为URL的参数

        String index = RpcContext.getContext().getAttachment("index"); // 获取客户端隐式传入的参数，用于框架集成，不建议常规业务使用
        System.out.println(String.format("%s, %s , %s, %s", isConsumerSide, serverIP, application, index));

        // ...

        return "hello " + name;
    }
}

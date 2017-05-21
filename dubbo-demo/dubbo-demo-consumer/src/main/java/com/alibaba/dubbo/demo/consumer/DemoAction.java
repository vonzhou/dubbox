/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.consumer;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.demo.bid.*;
import com.alibaba.dubbo.demo.hello.HelloService;
import com.alibaba.dubbo.demo.user.User;
import com.alibaba.dubbo.demo.user.facade.AnotherUserRestService;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.EchoService;

public class DemoAction {

    private HelloService helloService;

    private BidService bidService;

    private AnotherUserRestService anotherUserRestService;

    public void setBidService(BidService bidService) {
        this.bidService = bidService;
    }

    public void setAnotherUserRestService(AnotherUserRestService anotherUserRestService) {
        this.anotherUserRestService = anotherUserRestService;
    }

    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }

    public void start() throws Exception {
//        docDigest();

        RpcContext.getContext().setAttachment("index", "1"); // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
        helloService.hello("vonzhou");


        // ----- 以下原有代码   --------
        BidRequest request = new BidRequest();

        Impression imp = new Impression();
        imp.setBidFloor(1.1);
        imp.setId("abc");
        List<Impression> imps = new ArrayList<Impression>(1);
        imps.add(imp);
        request.setImpressions(imps);

        Geo geo = new Geo();
        geo.setCity("beijing");
        geo.setCountry("china");
        geo.setLat(100.1f);
        geo.setLon(100.1f);

        Device device = new Device();
        device.setMake("apple");
        device.setOs("ios");
        device.setVersion("7.0");
        device.setLang("zh_CN");
        device.setModel("iphone");
        device.setGeo(geo);
        request.setDevice(device);

//        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            System.out.println(bidService.bid(request).getId());
            System.out.println("SUCCESS: got bid response id: " + bidService.bid(request).getId());
        }

//        System.out.println(">>>>> Total time consumed:" + (System.currentTimeMillis() - start));
        boolean isConsumerSide = RpcContext.getContext().isConsumerSide(); // 本端是否为消费端，这里会返回true
        String serverIP = RpcContext.getContext().getRemoteHost(); // 获取最后一次调用的提供方IP地址
        String application = RpcContext.getContext().getUrl().getParameter("application"); // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
        System.out.println(String.format("%s, %s , %s", isConsumerSide, serverIP, application));
        // ...

        try {
            bidService.throwNPE();
            System.out.println("ERROR: no exception found");
        } catch (NullPointerException e) {
            System.out.println("SUCCESS: caught exception " + e.getClass());
        }

        User user = new User(1L, "larrypage");
        System.out.println("SUCCESS: registered user with id " + anotherUserRestService.registerUser(user).getId());

        RpcContext.getContext().setAttachment("clientName", "demo");
        RpcContext.getContext().setAttachment("clientImpl", "dubbox");
        System.out.println("SUCCESS: got user " + anotherUserRestService.getUser(1L));
    }

    // 阅读文档过程写的
    public void docDigest() {
        EchoService echoService = (EchoService) bidService;
        System.out.println(echoService.$echo("OK"));

    }

}
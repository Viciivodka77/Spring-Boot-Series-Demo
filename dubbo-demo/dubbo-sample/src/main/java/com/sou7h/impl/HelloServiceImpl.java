package com.sou7h.impl;

import com.sou7h.api.HelloService;
import org.apache.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月22日 10:18 下午
 */
public class HelloServiceImpl implements HelloService {


    @Override
    public String sayHello(String anyStr) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + anyStr +
                ", request from consumer: " + RpcContext.getServiceContext().getRemoteAddress());
        return "Hello " + anyStr + ", response from provider: " + RpcContext.getServiceContext().getLocalAddress();
    }


}

package com.sou7h;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author sou7h
 * @description 方法提供者
 * @date 2022年02月22日 10:37 下午
 */
public class Provider {


    public static void main(String[] args) throws IOException {
        //1.获取到Spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/provider.xml");
        //2.开启服务
        context.start();
        System.out.println("dubbo com.sou7h.kafkareceiveserver.service started");
        //3.堵塞主线程
        System.in.read();
    }


}

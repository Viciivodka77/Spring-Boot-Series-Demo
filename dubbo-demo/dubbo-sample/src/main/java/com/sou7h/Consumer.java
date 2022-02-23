package com.sou7h;

import com.sou7h.api.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月23日 9:47 下午
 */
public class Consumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/consumer.xml");
        context.start();
        //获取bean 这里的属性name与xml中的id相对应
        HelloService helloService = (HelloService) context.getBean("helloService");
        helloService.sayHello();
    }

}
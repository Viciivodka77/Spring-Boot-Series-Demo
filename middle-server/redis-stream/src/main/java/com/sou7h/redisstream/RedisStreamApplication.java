package com.sou7h.redisstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sou7h
 * @description redis stream实现消息队列
 * @date 2022年12月19日 4:04 下午
 */
@SpringBootApplication
public class RedisStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisStreamApplication.class, args);
    }

}

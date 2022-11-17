package com.sou7h.kafkareceiveserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sou7h
 * @description Kafka接收启动类
 * @date 2022年10月21日 10:09 上午
 */
@SpringBootApplication
public class KafkaReceiveServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaReceiveServerApplication.class,args);
    }

}

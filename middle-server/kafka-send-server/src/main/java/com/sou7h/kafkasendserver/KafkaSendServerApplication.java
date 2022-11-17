package com.sou7h.kafkasendserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sou7h
 * @description kafka发送启动类
 * @date 2022年10月21日 10:09 上午
 */
@SpringBootApplication
public class KafkaSendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSendServerApplication.class,args);
    }

}

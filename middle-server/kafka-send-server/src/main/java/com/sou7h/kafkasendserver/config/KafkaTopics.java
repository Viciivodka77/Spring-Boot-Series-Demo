package com.sou7h.kafkasendserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author sou7h
 * @description kafka创建topic配置
 * @date 2022年10月25日 10:31 下午
 */
@Component
public class KafkaTopics {


    @Bean
    public NewTopic quickEventTopic(){
        return new NewTopic("quickEventTopic",1, (short) 1);
    }

}

package com.sou7h.redisstream.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author sou7h
 * @description 这里适用于 监听处理 一般使用这个自动化处理
 * @date 2022年12月20日 1:49 下午
 */
@Component
public class ReceiverMessageListener implements StreamListener<String, ObjectRecord<String, Object>> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(ObjectRecord<String, Object> message) {
        // 接收到消息
        System.out.println("message id " + message.getId());
        System.out.println("stream " + message.getStream());
        System.out.println("body " + message.getValue());


        //todo something

        // 消费完成后确认消费（ACK）
        // 通过RedisTemplate手动确认消息，确认之后消息会从队列中消失，如果不确认，可能存在重复消费
        //Long acknowledge = redisTemplate.opsForStream().acknowledge("group-01", message);
        //if (acknowledge > 0) {
        //    System.out.println("acknowledge的值是：" + acknowledge);
        //}
    }

}

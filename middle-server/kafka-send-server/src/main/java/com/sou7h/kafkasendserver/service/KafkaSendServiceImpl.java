package com.sou7h.kafkasendserver.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


/**
 * @author sou7h
 * @description kafka发送服务层实现
 * @date 2022年10月21日 1:39 下午
 */
@Slf4j
@Service
public class KafkaSendServiceImpl implements KafkaSendService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String msg,String topic,String key,String groupId){
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                topic == null || "".equals(topic) ? "quickstart-event":topic,
                key,
                msg
        );

        ListenableFuture<SendResult<String, String>> sendResultFuture = kafkaTemplate.send(producerRecord);

        sendResultFuture.addCallback(new KafkaSendCallback<String,String>(){

            @Override
            public void onSuccess(SendResult<String, String> sendResult) {
                log.info("发送成功,result={}",sendResult);
            }

            @Override
            public void onFailure(KafkaProducerException e) {
                log.error("发送失败",e);
                //doSomething
            }
        });



    }



}

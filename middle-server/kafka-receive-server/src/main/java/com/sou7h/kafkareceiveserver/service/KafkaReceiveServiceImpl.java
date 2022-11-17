package com.sou7h.kafkareceiveserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author sou7h
 * @description kafka接收服务实现层
 * @date 2022年10月25日 12:09 下午
 */
@Slf4j
@Service
public class KafkaReceiveServiceImpl implements KafkaReceiveService{

    @Override
    @KafkaListener(id = "foo", topics = "quickstart-events", clientIdPrefix = "myClientId")
    public void receive(String data) {
        log.info("Received data: {}",data);
    }
}

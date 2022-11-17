package com.sou7h.kafkasendserver.controller;

import com.sou7h.kafkasendserver.service.KafkaSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sou7h
 * @description kafka发送接口层
 * @date 2022年10月24日 12:03 下午
 */
@RestController
public class KafkaController {

    @Autowired
    private KafkaSendService kafkaService;

    @GetMapping("/send")
    public String sendMsg(String message,String topic,String key,String groupId) {
        kafkaService.send(message, topic, key, groupId);
        return "success";
    }



}

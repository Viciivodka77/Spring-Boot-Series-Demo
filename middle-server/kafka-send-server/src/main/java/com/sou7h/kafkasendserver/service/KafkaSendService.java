package com.sou7h.kafkasendserver.service;

/**
 * @author sou7h
 * @description kafka发送服务层
 * @date 2022年10月24日 11:59 上午
 */
public interface KafkaSendService {

    void send(String msg,String topic,String key,String groupId);

}

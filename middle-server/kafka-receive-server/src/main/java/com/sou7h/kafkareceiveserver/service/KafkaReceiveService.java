package com.sou7h.kafkareceiveserver.service;

/**
 * @author sou7h
 * @description kafka接收服务层
 * @date 2022年10月25日 12:09 下午
 */
public interface KafkaReceiveService {

    void receive(String data);

}

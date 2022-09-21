package com.sou7h.demo.service;

/**
 * @author sou7h
 * @description 推送消息service
 * @date 2022年09月21日 12:20 下午
 */
public interface PushService {

    void pushMsgToOne(String userId, String message);

    void pushMsgToAll(String message);

}

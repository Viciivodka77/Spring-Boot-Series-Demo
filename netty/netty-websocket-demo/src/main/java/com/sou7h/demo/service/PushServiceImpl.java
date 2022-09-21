package com.sou7h.demo.service;

import com.sou7h.demo.config.NettyConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sou7h
 * @description 推送消息service
 * @date 2022年09月21日 12:21 下午
 */
@Service
public class PushServiceImpl implements PushService {

    @Override
    public void pushMsgToOne(String userId, String message) {
        ConcurrentHashMap<String, Channel> userChannelMap = NettyConfig.getUserChannelMap();
        Channel channel = userChannelMap.get(userId);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    @Override
    public void pushMsgToAll(String message) {
        NettyConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(message));
    }
}

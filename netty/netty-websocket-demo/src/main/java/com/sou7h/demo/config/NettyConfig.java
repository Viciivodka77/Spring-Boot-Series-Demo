package com.sou7h.demo.config;


import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sou7h
 * @description Netty配置
 * @date 2022年09月21日 11:32 上午
 */
@NoArgsConstructor
public class NettyConfig {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public static ConcurrentHashMap<String, Channel> getUserChannelMap() {
        return userChannelMap;
    }


}

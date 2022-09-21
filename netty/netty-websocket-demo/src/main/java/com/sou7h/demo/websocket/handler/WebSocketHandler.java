package com.sou7h.demo.websocket.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sou7h.demo.config.NettyConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sou7h
 * @description 处理器
 * @date 2022年09月21日 11:46 上午
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //处理接收消息
        log.info("接收信息:{}",msg.text());

        JSONObject jsonObject = JSON.parseObject(msg.text());
        String uid = jsonObject.getString("uid");
        NettyConfig.getUserChannelMap().put(uid, ctx.channel());

        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");

        ctx.channel().attr(userIdKey).setIfAbsent(uid);

        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器连接成功"));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded 被调用 {}",ctx.channel().id().asLongText());
        //添加到channelGroup通道组
        NettyConfig.getChannelGroup().add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved 被调用 {}",ctx.channel().id().asLongText());
        NettyConfig.getChannelGroup().remove(ctx.channel());
        //移除userid
        removeUserId(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("handlerException异常",cause);
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();

    }

    private void removeUserId(ChannelHandlerContext ctx){
        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(userIdKey).get();
        log.info("removing user:{}", userId);
        NettyConfig.getUserChannelMap().remove(userId);
    }

}

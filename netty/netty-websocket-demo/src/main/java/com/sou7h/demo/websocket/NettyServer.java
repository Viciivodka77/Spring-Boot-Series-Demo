package com.sou7h.demo.websocket;

import com.sou7h.demo.websocket.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年09月21日 11:42 上午
 */
@Slf4j
@Component
public class NettyServer {

    private static final String WEBSOCKET_PROTOCOL = "WebSocket";

    @Value("${websocket.netty.port}")
    private int port;

    @Value("${webSocket.netty.path:/webSocket}")
    private String webSocketPath;

    @Autowired
    private WebSocketHandler websocketHandler;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init(){
        new Thread(() -> {
            try {
                start();
            } catch (InterruptedException e) {
                log.error("InterruptedException while starting", e);
            }
        }).start();
    }

    @PreDestroy
    public void destroy() throws Exception{
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().sync();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().sync();
        }
    }


    private void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec())
                                .addLast(new ObjectEncoder())
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(8192))
                                .addLast(new WebSocketServerProtocolHandler(webSocketPath,WEBSOCKET_PROTOCOL,true,65536*10))
                                .addLast(websocketHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind().sync();
        log.info("Server started and listen on :{}",channelFuture.channel().localAddress());
        channelFuture.channel().closeFuture().sync();
    }

}

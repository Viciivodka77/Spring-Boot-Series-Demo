package com.sou7h.redisstream.config;

import com.sou7h.redisstream.handler.ReceiverErrorHandler;
import com.sou7h.redisstream.listener.ReceiverMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sou7h
 * @description 配置监听
 * @date 2022年12月20日 2:56 下午
 */
@Configuration
public class ReceiverConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private ReceiverErrorHandler receiverErrorHandler;

    @Autowired
    private ReceiverMessageListener receiverMessageListener;

    /**
     * 能够同时反对 独立生产 和 消费者组 生产
     * <p>
     * 能够反对动静的 减少和删除 消费者
     * <p>
     * 生产组须要事后创立进去
     *
     * @return StreamMessageListenerContainer
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, ObjectRecord<String, Object>> streamMessageListenerContainer() {
        AtomicInteger index = new AtomicInteger(1);
        int processors = Runtime.getRuntime().availableProcessors();

        //需要一个线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(processors, processors, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("async-stream-consumer-" + index.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, Object>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 一次最多获取多少条音讯
                        .batchSize(10)
                        // 运行 Stream 的 poll task
                        .executor(executor)
                        // 能够了解为 Stream Key 的序列化形式
                        .keySerializer(RedisSerializer.string())
                        // 能够了解为 Stream 前方的字段的 key 的序列化形式
                        .hashKeySerializer(RedisSerializer.string())
                        // 能够了解为 Stream 前方的字段的 value 的序列化形式
                        .hashValueSerializer(RedisSerializer.string())
                        // Stream 中没有音讯时，阻塞多长时间，须要比 `spring.redis.timeout` 的工夫小
                        .pollTimeout(Duration.ofSeconds(1))
                        // ObjectRecord 时，将 对象的 filed 和 value 转换成一个 Map 比方：将Book对象转换成map
                        .objectMapper(new ObjectHashMapper())
                        // 获取音讯的过程或获取到音讯给具体的音讯者解决的过程中，产生了异样的解决
                        .errorHandler(receiverErrorHandler)
                        // 将发送到Stream中的Record转换成ObjectRecord，转换成具体的类型是这个中央指定的类型
                        //.targetType(Book.class)
                        .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, Object>> streamMessageListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);


        String streamKey = "stream-03";

        // 生产组A,不主动ack
        // 从生产组中没有调配给消费者的音讯开始生产
        streamMessageListenerContainer.receive(Consumer.from("group-01", "group-01-name-a"),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), receiverMessageListener);

        return streamMessageListenerContainer;
    }
}

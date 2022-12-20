package com.sou7h.redisstream.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

/**
 * @author sou7h
 * @description redis stream实现消息队列
 * @date 2022年12月19日 4:12 下午
 */
@Service
@Slf4j
public class StreamService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void publish(){
        log.info("开始推送数据");
        int i = 41;
        StreamOperations<String, Object, Object> streamOperations = redisTemplate.opsForStream();
        //String group = streamOperations.createGroup("stream-03", "group-01");
        //log.info("group create = {}",group);
        while(i <= 50){
            try {
                HashMap<Object, Object> content = new HashMap<>();
                content.put("count",i + "");
                streamOperations.add("stream-03", content);
                log.info("推送content={}", content);
                i++;
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @description 这里是手动receive 可以试着while true 但是会因为堵塞超时 异常报错
     */
    public void receive(){
        log.info("开始接收数据..");
        StreamOperations<String, Object, Object> streamOperations = redisTemplate.opsForStream();
        List<ObjectRecord<String, String>> read = streamOperations.read(String.class,
                Consumer.from("group-01", "group-01-a"), //customer name 需要自定义唯一
                StreamReadOptions.empty()
                        .count(1)//count 为每次获取的数量
                        .block(Duration.ZERO),// 如果没有数据，则阻塞1s 阻塞 须要小于`spring.redis.timeout`配置的
                                                //zero 为 始终阻塞直到获取数据，可能会报超时异样
                StreamOffset.create("stream-03", ReadOffset.lastConsumed()));
        log.info("result = {}",read);
        ObjectRecord<String, String> record;
        if (read != null) {
            record = read.get(0);
            //todo something

            //ack
            streamOperations.acknowledge("stream-03","group-01",record.getId());
        }
    }

    /**
     * @description 处理没有正常ack的数据
     * @return void
     */
    public void xpending(){
        // pending 列表为  消费组中消费了，但是没有被ack的列表才会进入。如果还没有消费，也不会进入。
        log.info("获取未ack的列表");
        StreamOperations<String, Object, Object> streamOperations = redisTemplate.opsForStream();
        PendingMessagesSummary pending = streamOperations.pending("stream-03", "group-01");
        if (pending != null){
            long totalPendingMessages = pending.getTotalPendingMessages();
            log.info("totalPendingMessages={}", totalPendingMessages);
            if (totalPendingMessages == 0){
                return;
            }
            String max = pending.maxMessageId();
            String min = pending.minMessageId();
            //获取所有消费者
            pending.getPendingMessagesPerConsumer().forEach((k,v) -> {
                //k 为消费者 v为消费者没确定的数量
                final int perCount = 10;
                if (v > 0){
                    //获取10个
                    long loopTimes = v / perCount + 1;
                    for (long i = 0; i < loopTimes; i++) {
                        PendingMessages pendingMessages = streamOperations.pending("stream-03", Consumer.from("group-01", k), Range.closed(min, max), perCount);
                        for (PendingMessage pendingMessage : pendingMessages) {
                            //消息id
                            RecordId id = pendingMessage.getId();
                            //从被消费者消费但未被确认 到 现在的时间
                            Duration elapsedTimeSinceLastDelivery = pendingMessage.getElapsedTimeSinceLastDelivery();
                            //消息被获取次数
                            long totalDeliveryCount = pendingMessage.getTotalDeliveryCount();
                            log.info("消息{},未被ack时间={},被重复获取次数={}",id,elapsedTimeSinceLastDelivery.getSeconds(), totalDeliveryCount);

                            //单个获取
                            List<MapRecord<String, Object, Object>> range = streamOperations.range("stream-03", Range.just(id.getValue()));
                            //todo 判断时间或者被获取次数 异常处理 重新消费 claim转移到其他消费者消费 确认/警告 或者其他

                            //确认消息ack
                            streamOperations.acknowledge("stream-03","group-01",id);
                        }
                    }

                }
            });
        }
    }





}

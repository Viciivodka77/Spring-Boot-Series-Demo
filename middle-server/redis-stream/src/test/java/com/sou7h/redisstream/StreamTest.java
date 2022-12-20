package com.sou7h.redisstream;

import com.sou7h.redisstream.service.StreamService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

import java.util.List;

/**
 * @author sou7h
 * @description 测试类
 * @date 2022年12月20日 9:34 上午
 */
@Slf4j
@SpringBootTest
public class StreamTest {

    @Autowired
    private StreamService streamService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void publish() {
        streamService.publish();
    }


    @Test
    public void receive() {
        streamService.receive();
    }

    @Test
    public void pending(){
        streamService.xpending();
    }


    @Test
    public void test(){
        StreamOperations<String, Object, Object> streamOperations = redisTemplate.opsForStream();
        List<MapRecord<String, Object, Object>> range = streamOperations.range("stream-03", Range.just("1671523622829-0"));
        log.info("range:{}" , range);

    }
}

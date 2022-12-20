package com.sou7h.redisstream.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * @author sou7h
 * @description 异常处理
 * @date 2022年12月20日 5:42 下午
 */
@Slf4j
@Component
public class ReceiverErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        log.error("出现异常", t);
    }
}

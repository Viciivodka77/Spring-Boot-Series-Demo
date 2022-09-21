package com.sou7h.demo.controller;

import com.sou7h.demo.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sou7h
 * @description 推送消息controller
 * @date 2022年09月21日 12:24 下午
 */
@RestController
@RequestMapping("/push")
public class PushController {

    @Autowired
    private PushService pushService;

    @PostMapping("/pushAll")
    public void pushToAll( String msg){
        pushService.pushMsgToAll(msg);
    }

    @PostMapping("/pushOne")
    public void pushToOne( String toUserId, String msg) {
        pushService.pushMsgToOne(toUserId, msg);
    }

}

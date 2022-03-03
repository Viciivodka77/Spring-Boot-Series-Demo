package com.sou7h.springbootdubbo.controller;

import com.sou7h.springbootdubbo.api.UserService;
import com.sou7h.springbootdubbo.entity.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年03月02日 10:22 下午
 */
@RestController
public class UserController {

    @DubboReference(version = "1.0.0")
    UserService userService;

    @RequestMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") String id){
        System.out.println(id);
        return userService.getUserById(id);
    }

}

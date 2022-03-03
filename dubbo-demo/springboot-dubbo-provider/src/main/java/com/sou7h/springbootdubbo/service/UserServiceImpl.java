package com.sou7h.springbootdubbo.service;

import com.sou7h.springbootdubbo.api.UserService;
import com.sou7h.springbootdubbo.entity.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年03月02日 9:54 下午
 */
@DubboService(version = "1.0.0")
@Service
public class UserServiceImpl implements UserService{

    /**
     * The default value of ${dubbo.application.name} is ${spring.application.name}
     */
    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public User getUserById(String id) {
        return new User("1","小明",18);
    }
}

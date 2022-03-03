package com.sou7h.springbootdubbo.api;


import com.sou7h.springbootdubbo.entity.User;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年03月02日 9:51 下午
 */
public interface UserService {

    User getUserById(String id);

}

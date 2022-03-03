package com.sou7h.springbootdubbo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年03月02日 9:52 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private String id;
    private String name;
    private Integer age;

}

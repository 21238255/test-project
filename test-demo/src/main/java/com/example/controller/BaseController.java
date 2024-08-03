package com.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marlon
 * @date 2024/07/30 21:31
 **/
@RestController
@RequestMapping("/base")
public class BaseController {


    @RequestMapping("/test")
    public String test() {
        return "Hello, World!";
    }



}

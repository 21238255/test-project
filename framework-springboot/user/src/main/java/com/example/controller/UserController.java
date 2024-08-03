package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marlon
 * @date 2024/08/03 14:53
 **/
@RestController
public class UserController {

    @RequestMapping("/user")
    public String getUser() {
        return "Hello, User!";
    }

}

package com.marlon.service;

import com.marlon.spring.MarlonApplicationContext;

/**
 * @author Marlon
 * @date 2024/08/03 11:13
 **/
public class Test {
    public static void main(String[] args) {
        MarlonApplicationContext marlonApplicationContext = new MarlonApplicationContext(AppConfig.class);
        UserService userService = (UserService) marlonApplicationContext.getBean("userService");

        userService.test();


    }
}

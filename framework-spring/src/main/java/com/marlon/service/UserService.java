package com.marlon.service;

import com.marlon.spring.*;

/**
 * @author Marlon
 * @date 2024/08/03 11:12
 **/
@Component()
public class UserService implements UserInterface {

    @Autowired
    private OrderService orderService;



    public void test() {
        System.out.println(orderService);
//        orderService.createOrder();
    }


}

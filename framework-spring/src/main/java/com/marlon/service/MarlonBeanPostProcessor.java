package com.marlon.service;

import com.marlon.spring.BeanPostProcessor;
import com.marlon.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Marlon
 * @date 2024/08/03 13:48
 **/
@Component
public class MarlonBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName){
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName){
        if ("userService".equals(beanName)) {
            Object proxyInstance = Proxy.newProxyInstance(MarlonBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("Before invoking method: " + method.getName());
                    System.out.println("AOP before invoking");
                    return method.invoke(bean, args);
                    // After invoking method, do something else here...
                    // return method.invoke(bean, args);
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}

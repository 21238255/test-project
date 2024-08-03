package com.marlon.spring;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object instance, String beanName) ;
}

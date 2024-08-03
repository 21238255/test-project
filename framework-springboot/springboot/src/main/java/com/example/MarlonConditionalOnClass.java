package com.example;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({MarlonCondition.class})
/**
 * 自定义 Spring Boot 条件注解
 * @author Marlon
 * @date 2024/08/03 15:02
 */

public @interface MarlonConditionalOnClass {
    String value();
}

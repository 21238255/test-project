package com.marlon.spring;


import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ComponentScan(basePackages = {"com.marlon.spring"})
public @interface Autowired {
    String value() default "";
}

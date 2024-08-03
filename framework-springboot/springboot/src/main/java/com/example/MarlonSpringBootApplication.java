package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Marlon
 * @date 2024/08/03 14:54
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan
@Import(AutoConfigurationImportSelector.class)
public @interface MarlonSpringBootApplication {


}

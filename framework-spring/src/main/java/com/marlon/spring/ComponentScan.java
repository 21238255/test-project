package com.marlon.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface ComponentScan {
    String value() default "";
    String[] basePackages() default {};
    String[] excludeBasePackages() default {};
    boolean includeFilters() default true;
    Class<?>[] excludeFilters() default {};
    String[] resourcePattern() default {};
    String[] name() default {};
    boolean lazyInit() default true;
    boolean useDefaultFilters() default true;
    Class<?>[] customFilters() default {};
    Class<?>[] customAnnotationTypes() default {};
    Class<?>[] customImport() default {};
    Class<?>[] customImportResource() default {};
    Class<?>[] customBeanClasses() default {};
    Class<?>[] customScope() default {};
    Class<?>[] customValue() default {};
    Class<?>[] customQualifier() default {};
    Class<?>[] customPrimary() default {};
    Class<?>[] customNestedCondition() default {};
    Class<?>[] customNestedConfiguration() default {};
    Class<?>[] customImportAware() default {};
    Class<?>[] customInitializingBean() default {};
    Class<?>[] customDisposableBean() default {};
}

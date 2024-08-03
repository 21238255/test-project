package com.example;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * @author Marlon
 * @date 2024/08/03 15:54
 **/
public class MarlonCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(MarlonConditionalOnClass.class.getName());
            String className = (String) annotationAttributes.get("value");
            Objects.requireNonNull(context.getClassLoader()).loadClass(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

package com.marlon.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marlon
 * @date 2024/08/03 11:14
 **/
public class MarlonApplicationContext{
    private final Class<?> configClass;
    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private final ArrayList<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public MarlonApplicationContext(Class<?> configClass) {
        if (configClass == null) {
            throw new IllegalArgumentException("Config class must not be null");
        }
        this.configClass = configClass;

        // 解析 bean 定义并注册
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
            String path = componentScan.value(); // com.marlon.service
            path = path.replace(".", "/"); // com/marlon/service
            // 扫描并注册 bean
            ClassLoader classLoader = MarlonApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);// 获取资源路径

            File file = new File(resource.getFile());
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    if (f.getName().endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("\\", ".");

                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (clazz.isAnnotationPresent(Component.class)) {

                                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                    BeanPostProcessor beanPostProcessorInstance = (BeanPostProcessor) clazz.newInstance();
                                    beanPostProcessors.add(beanPostProcessorInstance);
                                }
                                // Bean 定义
                                // 1. 定义 bean 名称
                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();
                                if ("".equals(beanName)) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                // 2. 定义 bean 类
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setBeanClass(clazz);
                                // 3. 定义 bean 作用域
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    // 4. 解析 bean 定义
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);// singleton
                                    beanDefinition.setScope(scopeAnnotation.value());
                                }else {
                                    beanDefinition.setScope("singleton");
                                }
                                // 5. 注册 bean
                                beanDefinitionMap.put(beanName, beanDefinition);


                            }
                        }catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }
        // 单例模式创建 bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }
    private Object createBean(String beanName, BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getBeanClass();
        try {
            Object instance = clazz.getConstructor().newInstance();

            // 依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = declaredField.getAnnotation(Autowired.class);
                    String autowiredBeanName = autowired.value();
                    if ("".equals(autowiredBeanName)) {
                        autowiredBeanName = Introspector.decapitalize(declaredField.getType().getSimpleName());
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(instance, getBean(autowiredBeanName));
                }
            }
            // 实现 BeanNameAware 接口
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            // 实现 BeanPostBeforeProcessor
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }
            // 实现 initializingBean 接口
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }
            // 实现 BeanPostAfterProcessor
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            // 循环依赖检查

            // 返回 bean
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }



    public Object getBean(String beanName){
        // 获取bean
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException("Bean '" + beanName + "' is not found.");
        }else {
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                // 单例模式
                Object bean = singletonObjects.get(beanName);
                if (bean == null) {
                    Object o = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, o);
                }
                return bean;
            }else {
                // 原型模式
                return createBean(beanName, beanDefinition);
            }
        }
    }
}

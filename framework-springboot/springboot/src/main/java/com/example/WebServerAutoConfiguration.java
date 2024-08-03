package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marlon
 * @date 2024/08/03 15:52
 **/
@Configuration
public class WebServerAutoConfiguration {
    // 自定义 Spring Boot Web Server 自动配置
    @Bean
    @MarlonConditionalOnClass("org.apache.catalina.startup.Tomcat")
    public TomcatWebServer tomcatWebServer() {
        // 自定义 TomcatWebServer 并返回
        return new TomcatWebServer();
    }

    @Bean
    @MarlonConditionalOnClass("org.eclipse.jetty.server.Server")
    public JettyWebServer jettyWebServer() {
        // 自定义 JettyWebServer 并返回
        return new JettyWebServer();
    }


}

package com.example;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.util.Map;

/**
 * @author Marlon
 * @date 2024/08/03 14:57
 **/
public class MarlonSpringApplication     {

    public static void run(Class<?> clazz) {
        // 启动 Tomcat 并运行 Spring Boot 应用
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(clazz);
        webApplicationContext.refresh();


//        startUpTomcat(webApplicationContext);
        WebServer webServer = getWebServer(webApplicationContext);
        webServer.start();


    }

    private static WebServer getWebServer(AnnotationConfigWebApplicationContext webApplicationContext) {
        // 获取 WebServer 实现
        Map<String, WebServer> beansOfType = webApplicationContext.getBeansOfType(WebServer.class);
        if (beansOfType.isEmpty()) {
            throw new IllegalStateException("No WebServer bean found in the context");
        }
        if (beansOfType.size() > 1) {
            throw new IllegalStateException("Multiple WebServer beans found in the context. Please specify a unique bean name");
        }
        return beansOfType.values().stream().findFirst().get();
    }

    private static void startUpTomcat(WebApplicationContext webApplicationContext) {
        // 启动 Tomcat 并运行 Spring Boot 应用
        // 实现 Tomcat 启动代码
        Tomcat tomcat = new Tomcat();
        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(8081);

        Engine engine = new StandardEngine();
        engine.setDefaultHost("localhost");

        Host host = new StandardHost();
        host.setName("localhost");

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet(webApplicationContext));
        context.addServletMappingDecoded("/*", "dispatcher");

        try{
            tomcat.start();
        }catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}

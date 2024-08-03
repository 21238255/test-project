package com.example;

/**
 * @author Marlon
 * @date 2024/08/03 15:41
 **/
public class JettyWebServer implements WebServer {
    @Override
    public void start() {
        System.out.println("Jetty started...");
    }
}

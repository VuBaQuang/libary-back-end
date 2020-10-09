package com.vbqkma.libarybackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
@EnableConfigurationProperties
public class LibaryBackEndApplication {
    public static ApplicationContext ctx;

    @Autowired
    private void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }
    public static void main(String[] args) {
        SpringApplication.run(LibaryBackEndApplication.class, args);
    }
}

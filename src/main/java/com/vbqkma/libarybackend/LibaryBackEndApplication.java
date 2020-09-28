package com.vbqkma.libarybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class LibaryBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibaryBackEndApplication.class, args);
    }
}

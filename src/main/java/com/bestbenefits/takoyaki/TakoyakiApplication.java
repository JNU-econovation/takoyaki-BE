package com.bestbenefits.takoyaki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TakoyakiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakoyakiApplication.class, args);
    }
}

package com.ofl.promotion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.ofl")
@MapperScan("com.ofl.**")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

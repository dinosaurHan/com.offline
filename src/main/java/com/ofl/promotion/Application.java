package com.ofl.promotion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ofl")
@MapperScan({"com.ofl.promotion.manage.organize.mapper","com.ofl.promotion.manage.emp.mapper",
        "com.ofl.promotion.manage.store.mapper","com.ofl.promotion.manage.guide.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

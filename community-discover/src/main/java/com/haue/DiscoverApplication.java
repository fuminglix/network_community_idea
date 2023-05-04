package com.haue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.haue")
@MapperScan("com.haue.mapper")
public class DiscoverApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoverApplication.class,args);
    }
}

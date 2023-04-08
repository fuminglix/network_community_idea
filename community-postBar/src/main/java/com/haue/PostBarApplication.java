package com.haue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.haue.mapper")
public class PostBarApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostBarApplication.class,args);
    }
}

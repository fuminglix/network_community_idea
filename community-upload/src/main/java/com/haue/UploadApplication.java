package com.haue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.haue.mapper")
@SpringBootApplication
public class UploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class,args);
    }
}

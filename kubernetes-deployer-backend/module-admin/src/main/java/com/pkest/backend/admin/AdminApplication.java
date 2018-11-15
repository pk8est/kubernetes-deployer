package com.pkest.backend.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.io.IOException;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(AdminApplication.class, args);
    }
}

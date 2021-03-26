package com.xl.study.studytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@RestController
@SpringBootApplication
public class StudytestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudytestApplication.class, args);
    }
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @RequestMapping("/test")
    public Object test(){
        return "test";
    }
}

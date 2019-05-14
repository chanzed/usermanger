package com.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.course.controller, com.course.config")
public class Application {
    public static   void main(String[] agrs){
        SpringApplication.run(Application.class, agrs);
    }
}

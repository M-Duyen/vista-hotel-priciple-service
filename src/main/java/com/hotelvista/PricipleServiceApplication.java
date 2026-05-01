package com.hotelvista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PricipleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PricipleServiceApplication.class, args);
    }
}


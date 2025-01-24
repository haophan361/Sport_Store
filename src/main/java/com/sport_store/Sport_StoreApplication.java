package com.sport_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Sport_StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sport_StoreApplication.class, args);
    }

}

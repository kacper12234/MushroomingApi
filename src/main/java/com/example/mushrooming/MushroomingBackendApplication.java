package com.example.mushrooming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MushroomingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MushroomingBackendApplication.class, args);
    }

}

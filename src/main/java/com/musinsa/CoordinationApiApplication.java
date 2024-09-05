package com.musinsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CoordinationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoordinationApiApplication.class, args);
    }

}

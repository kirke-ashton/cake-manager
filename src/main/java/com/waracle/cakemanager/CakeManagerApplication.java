package com.waracle.cakemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.waracle.cakemanager"})
public class CakeManagerApplication {

    public static void main(String... args) {
        SpringApplication.run(CakeManagerApplication.class, args);
    }
}

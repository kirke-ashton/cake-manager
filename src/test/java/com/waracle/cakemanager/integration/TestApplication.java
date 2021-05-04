package com.waracle.cakemanager.integration;

import com.waracle.cakemanager.CakeManagerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.waracle.cakemanager"})
@EnableJpaRepositories(basePackages = {"com.waracle.cakemanager.repository"})
@Import(TestConfiguration.class)
@Profile("waracle-integration-test")
public class TestApplication {

    public static void main(String... args) {
        SpringApplication.run(CakeManagerApplication.class, args);
    }
}

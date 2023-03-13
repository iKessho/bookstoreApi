package com.example.bookstoreApi.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class FakerConfig {
    @Bean
    @Scope("singleton")
    public Faker faker() {
        return new Faker();
    }
}
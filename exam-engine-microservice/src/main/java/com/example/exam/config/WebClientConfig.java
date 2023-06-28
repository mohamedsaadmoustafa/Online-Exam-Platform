package com.example.exam.config;

import io.helidon.webclient.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder myWebClientBuilder() {
        return WebClient.builder();
    }
}
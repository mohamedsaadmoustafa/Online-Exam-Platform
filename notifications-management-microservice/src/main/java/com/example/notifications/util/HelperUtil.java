package com.example.notifications.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HelperUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelperUtil.class);

    private final WebClient webClient;

    @Value("${user.microservice.url}")
    String userServiceURL;

    public HelperUtil() {
        this.webClient = WebClient.create();
    }

    public String getFirebaseToken(String username) {
        String url = "http://" + userServiceURL + "/" + username + "/token";
        String response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (response != null) {
            return response;
        } else {
            LOGGER.error("Received null response from the API.");
            throw new IllegalStateException("Received null response from the API.");
        }
    }
}
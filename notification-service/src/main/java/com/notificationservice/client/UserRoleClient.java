package com.notificationservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class UserRoleClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String IDENTITY_URL = "http://localhost:8082/api/auth/users/{userId}/role";

    public String getUserRole(Long userId) {
        try {
            Map<?, ?> response = restTemplate.getForObject(IDENTITY_URL, Map.class, userId);
            return response != null ? (String) response.get("role") : null;
        } catch (Exception e) {
            log.warn("Could not fetch role for userId {}: {}", userId, e.getMessage());
            return null;
        }
    }
}

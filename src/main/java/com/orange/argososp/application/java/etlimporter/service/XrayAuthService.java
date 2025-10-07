package com.orange.argososp.application.java.etlimporter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class XrayAuthService {
    @Value("${xray.base-url}")
    private String baseUrl;
    @Value("${xray.client-id}")
    private String clientId;
    @Value("${xray.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAuthToken() {
        String authUrl = baseUrl + "/authenticate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"client_id\":\"" + clientId + "\", \"client_secret\":\"" + clientSecret + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(authUrl, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().replace("\"", "");  // Token es un string simple
        }
        throw new RuntimeException("Autenticación fallida");
    }
}

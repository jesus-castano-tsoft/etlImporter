package com.orange.argososp.application.java.etlimporter.service;

import com.orange.argososp.application.java.etlimporter.model.TestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class XrayGraphQLClient {
    @Value("${xray.base-url}")
    private String baseUrl;
    @Value("${xray.graphql-limit:50}")
    private int limit;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestResponse getTests(String jql, String token, int start) {
        String graphqlUrl = baseUrl + "/graphql";

        String query = String.format(
                "query { getTests(jql: \"%s\", limit: %d, start: %d) { results { issueId projectId jira testType { name } folder { path } steps { id action data result } } total } }",
                jql.replace("\"", "\\\""), limit, start);

        Map<String, String> body = Map.of("query", query);
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing GraphQL body", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        System.out.println("Sending GraphQL query: " + jsonBody); // Log query

        ResponseEntity<String> response = restTemplate.postForEntity(graphqlUrl, entity, String.class);
        System.out.println("Raw API response: " + response.getBody()); // Log raw response

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                TestResponse testResponse = objectMapper.readValue(response.getBody(), TestResponse.class);
                if (testResponse.getErrors() != null && !testResponse.getErrors().isEmpty()) {
                    throw new RuntimeException("GraphQL errors: " + testResponse.getErrors().stream()
                            .map(TestResponse.Error::getMessage).reduce("", (a, b) -> a + ", " + b));
                }
                return testResponse;
            } catch (Exception e) {
                throw new RuntimeException("Error parsing GraphQL response: " + response.getBody(), e);
            }
        }
        throw new RuntimeException("Error fetching tests: " + response.getBody());
    }
}

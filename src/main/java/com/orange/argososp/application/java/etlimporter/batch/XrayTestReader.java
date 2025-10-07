package com.orange.argososp.application.java.etlimporter.batch;

import com.orange.argososp.application.java.etlimporter.model.Test;
import com.orange.argososp.application.java.etlimporter.service.XrayAuthService;
import com.orange.argososp.application.java.etlimporter.service.XrayGraphQLClient;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

public class XrayTestReader implements ItemReader<Test> {
    private final String jql;
    private final XrayAuthService authService;
    private final XrayGraphQLClient graphqlClient;
    private Iterator<Test> currentBatchIterator;
    private int currentOffset = 0;
    private String token;

    public XrayTestReader(XrayAuthService authService, XrayGraphQLClient graphqlClient) {
        this.jql = "issuetype = Test"; // Fetch all tests, no project filter
        this.authService = authService;
        this.graphqlClient = graphqlClient;
        System.out.println("Using JQL: " + jql); // Log JQL
    }

    @Override
    public Test read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        if (token == null) {
            token = authService.getAuthToken();
            System.out.println("Obtained auth token: " + (token != null ? "success" : "null"));
        }

        if (currentBatchIterator == null || !currentBatchIterator.hasNext()) {
            System.out.println("Fetching tests with offset: " + currentOffset);
            var response = graphqlClient.getTests(jql, token, currentOffset);
            List<Test> results = response.getData().getGetTests().getResults();
            int totalTests = response.getData().getGetTests().getTotal();
            System.out.println("Received " + results.size() + " tests, total: " + totalTests);
            currentBatchIterator = results.iterator();
            currentOffset += results.size();
        }

        if (currentBatchIterator.hasNext()) {
            return currentBatchIterator.next();
        }
        return null; // End of batch
    }
}
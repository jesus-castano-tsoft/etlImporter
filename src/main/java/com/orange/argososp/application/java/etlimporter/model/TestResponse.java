package com.orange.argososp.application.java.etlimporter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResponse {
    @JsonProperty("data")
    private Data data;
    @JsonProperty("errors")
    private List<Error> errors;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public static class Data {
        @JsonProperty("getTests")
        private GetTests getTests;

        public GetTests getGetTests() {
            return getTests;
        }

        public void setGetTests(GetTests getTests) {
            this.getTests = getTests;
        }
    }

    public static class GetTests {
        private List<Test> results;
        private int total;

        public List<Test> getResults() {
            return results;
        }

        public void setResults(List<Test> results) {
            this.results = results;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private String message;
        private List<Location> locations;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Location> getLocations() {
            return locations;
        }

        public void setLocations(List<Location> locations) {
            this.locations = locations;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private int line;
        private int column;

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }
    }
}
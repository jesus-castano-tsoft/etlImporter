package com.orange.argososp.application.java.etlimporter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Test {
    private String issueId;
    private String projectId;
    @JsonProperty("jira")
    private Map<String, Object> jira;
    @JsonProperty("testType")
    private TestType testType;
    @JsonProperty("folder")
    private Folder folder;
    private List<Step> steps;

    // Getters and setters
    public String getIssueId() { return issueId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public Map<String, Object> getJira() { return jira; }
    public void setJira(Map<String, Object> jira) { this.jira = jira; }
    public TestType getTestType() { return testType; }
    public void setTestType(TestType testType) { this.testType = testType; }
    public Folder getFolder() { return folder; }
    public void setFolder(Folder folder) { this.folder = folder; }
    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    public static class TestType {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Folder {
        private String path;
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
    }

    public static class Step {
        private String id;
        private String action;
        private String data;
        private String result;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
    }
}
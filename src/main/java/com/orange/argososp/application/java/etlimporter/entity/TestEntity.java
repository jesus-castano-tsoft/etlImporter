package com.orange.argososp.application.java.etlimporter.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "xray_tests")
public class TestEntity {
    @Id
    private String issueId;
    private String projectId;
    private String jiraKey;
    private String jiraSummary;
    private String testType;
    private String folderPath;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> jira;

    @ElementCollection
    @CollectionTable(name = "xray_test_steps", joinColumns = @JoinColumn(name = "test_id"))
    private List<Step> steps;

    // Getters and setters
    public String getIssueId() { return issueId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getJiraKey() { return jiraKey; }
    public void setJiraKey(String jiraKey) { this.jiraKey = jiraKey; }
    public String getJiraSummary() { return jiraSummary; }
    public void setJiraSummary(String jiraSummary) { this.jiraSummary = jiraSummary; }
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    public String getFolderPath() { return folderPath; }
    public void setFolderPath(String folderPath) { this.folderPath = folderPath; }
    public Map<String, Object> getJira() { return jira; }
    public void setJira(Map<String, Object> jira) { this.jira = jira; }
    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    @Embeddable
    public static class Step {
        private String stepId;
        private String action;
        private String data;
        private String result;

        public String getStepId() { return stepId; }
        public void setStepId(String stepId) { this.stepId = stepId; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
    }
}
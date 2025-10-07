package com.orange.argososp.application.java.etlimporter.config;

import com.orange.argososp.application.java.etlimporter.batch.XrayTestReader;
import com.orange.argososp.application.java.etlimporter.entity.TestEntity;
import com.orange.argososp.application.java.etlimporter.model.Test;
import com.orange.argososp.application.java.etlimporter.repository.TestRepository;
import com.orange.argososp.application.java.etlimporter.service.XrayAuthService;
import com.orange.argososp.application.java.etlimporter.service.XrayGraphQLClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job fetchTestsJob(JobRepository jobRepository, Step fetchTestsStep) {
        return new JobBuilder("fetchTestsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fetchTestsStep)
                .build();
    }

    @Bean
    public Step fetchTestsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                               ItemReader<Test> testReader, ItemProcessor<Test, TestEntity> testProcessor,
                               ItemWriter<TestEntity> testWriter) {
        return new StepBuilder("fetchTestsStep", jobRepository)
                .<Test, TestEntity>chunk(10, transactionManager)
                .reader(testReader)
                .processor(testProcessor)
                .writer(testWriter)
                .build();
    }

    @Bean
    public ItemReader<Test> testReader(XrayAuthService authService, XrayGraphQLClient graphqlClient) {
        return new XrayTestReader(authService, graphqlClient);
    }

    @Bean
    public ItemProcessor<Test, TestEntity> testProcessor() {
        return test -> {
            TestEntity entity = new TestEntity();
            entity.setIssueId(test.getIssueId());
            entity.setProjectId(test.getProjectId());
            entity.setJira(test.getJira());
            entity.setJiraKey(test.getJira() != null ? (String) test.getJira().get("key") : null);
            entity.setJiraSummary(test.getJira() != null ? (String) test.getJira().get("summary") : null);
            entity.setTestType(test.getTestType().getName());
            entity.setFolderPath(test.getFolder() != null ? test.getFolder().getPath() : "");
            entity.setSteps(test.getSteps().stream().map(step -> {
                TestEntity.Step entityStep = new TestEntity.Step();
                entityStep.setStepId(step.getId());
                entityStep.setAction(step.getAction());
                entityStep.setData(step.getData());
                entityStep.setResult(step.getResult());
                return entityStep;
            }).toList());
            return entity;
        };
    }

    @Bean
    public ItemWriter<TestEntity> testWriter(TestRepository testRepository) {
        return testRepository::saveAll;
    }
}
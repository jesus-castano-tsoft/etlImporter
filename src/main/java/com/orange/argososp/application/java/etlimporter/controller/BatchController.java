package com.orange.argososp.application.java.etlimporter.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job fetchTestsJob;

    @PostMapping("/run-batch")
    public ResponseEntity<String> runBatch() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(fetchTestsJob, params);
            return ResponseEntity.ok("Batch job iniciado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al iniciar job: " + e.getMessage());
        }
    }
}

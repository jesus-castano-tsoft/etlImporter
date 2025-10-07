package com.orange.argososp.application.java.etlimporter.repository;

import com.orange.argososp.application.java.etlimporter.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, String> {
}

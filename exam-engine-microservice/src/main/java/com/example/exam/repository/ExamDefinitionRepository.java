package com.example.exam.repository;

import com.example.exam.model.ExamDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamDefinitionRepository extends JpaRepository<ExamDefinition, Long> {
}

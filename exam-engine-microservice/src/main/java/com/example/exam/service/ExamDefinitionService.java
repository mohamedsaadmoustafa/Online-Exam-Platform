package com.example.exam.service;

import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamDefinition;
import com.example.exam.repository.ExamDefinitionRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamDefinitionService {

    private final ExamDefinitionRepository examDefinitionRepository;
    public ExamDefinitionService(ExamDefinitionRepository examDefinitionRepository) {
        this.examDefinitionRepository = examDefinitionRepository;
    }

    public List<ExamDefinition> fetchAllExamDefinitions() throws ExamException {
        try {
            return examDefinitionRepository.findAll();
        } catch (Exception ex) {
            throw new ExamException("Failed to fetch all Exam Definitions");
        }
    }

    public ExamDefinition createExamDefinition(@NonNull ExamDefinition examDefinition) throws ExamException {
        try {
            return examDefinitionRepository.save(examDefinition);
        } catch (Exception ex) {
            throw new ExamException(String.format("Failed to create Exam Definition with name %s", examDefinition.getName()));
        }
    }

    public Optional<ExamDefinition> getExamDefinitionById(Long id) throws ExamException {
        try {
            return examDefinitionRepository.findById(id);
        } catch (Exception ex) {
            throw new ExamException(String.format("Failed to retrieve Exam Definition with Id %d", id));
        }
    }
}

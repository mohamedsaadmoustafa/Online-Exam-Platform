package com.example.exam.controller;

import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamDefinition;
import com.example.exam.service.ExamDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin
public class ExamDefinitionController {

    private final ExamDefinitionService examDefinitionService;
    public ExamDefinitionController(ExamDefinitionService examDefinitionService) {
        this.examDefinitionService = examDefinitionService;
    }

    // List Exam Definitions
    @GetMapping("/exam-definition")
    public ResponseEntity<List<ExamDefinition>> getAllExamDefinitions() throws ExamException {
        try {
            List<ExamDefinition> examDefinitions = examDefinitionService.fetchAllExamDefinitions();
            return ResponseEntity.ok(examDefinitions);
        } catch (RuntimeException ex) {
            throw new ExamException("Failed to fetch all Exam Definitions");
        }
    }

    // Create new ExamDefinition
    @PostMapping("/exam-definition")
    public ResponseEntity<ExamDefinition> createExamDefinition(@RequestBody ExamDefinition examDefinition) throws ExamException {
        try {
            ExamDefinition createdExamDefinition = examDefinitionService.createExamDefinition(examDefinition);
            return ResponseEntity.ok(createdExamDefinition);
        } catch (RuntimeException ex) {
            throw new ExamException(String.format("Failed to create Exam Definition with name %s", examDefinition.getName()));
        }
    }

    // get Exam Definition By Id
    @GetMapping("/exam-definition/{id}")
    public ResponseEntity<ExamDefinition> getExamDefinitionById(@PathVariable Long id) throws ExamException {
        Optional<ExamDefinition> examDefinition = examDefinitionService.getExamDefinitionById(id);
        if (examDefinition.isPresent()) {
            return ResponseEntity.ok(examDefinition.get());
        } else {
            throw new ExamException(String.format("Failed to retrieve Exam Definition with Id %d", id));
        }
    }
}
package com.example.exam.controller;

import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamInstance;
import com.example.exam.service.ExamInstanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/")
//@CrossOrigin
@CrossOrigin(origins = "https://localhost:4201/")
public class ExamInstanceController {

    private final ExamInstanceService examInstanceService;
    public ExamInstanceController(ExamInstanceService examInstanceService) {
        this.examInstanceService = examInstanceService;
    }

    @PostMapping("/assign-exam-to-student")
        public ResponseEntity<ExamInstance> assignExamToStudent(@RequestBody ExamInstance examInstance) {
        try {
            ExamInstance createdExamInstance = examInstanceService.createExamInstance(examInstance);
            return ResponseEntity.ok(createdExamInstance);
        } catch (ExamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/exam-instances")
    public ResponseEntity<Set<ExamInstance>> getAllExamInstances() {
        try {
            Set<ExamInstance> examInstances = examInstanceService.getAllExamInstances();
            return ResponseEntity.ok(examInstances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/students/{studentId}/exam-instances")
    public ResponseEntity<Set<ExamInstance>> getExamInstancesForStudent(@PathVariable String studentId) {
        try {
            Set<ExamInstance> examInstances = examInstanceService.getCurrentAndUnfinishedExams(studentId);
            return ResponseEntity.ok(examInstances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
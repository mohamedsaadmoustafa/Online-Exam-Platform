package com.example.exam.controller;

import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamInstance;
import com.example.exam.service.StartExamService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Set;


@RestController
@RequestMapping("/")
@CrossOrigin
public class StartExamController {

    private final StartExamService startExamService;

    @Autowired
    public StartExamController(StartExamService startExamService) {
        this.startExamService = startExamService;
    }


    // start exam
    @GetMapping("/exam-gateway/{generatedLinkUrl}")
    public ResponseEntity<Object> getExamInstanceByGeneratedLink(@PathVariable String generatedLinkUrl) {
        try {
            if (generatedLinkUrl != null) {
                Set<JsonNode> examForm = startExamService.startExam(generatedLinkUrl);
                return new ResponseEntity<>(examForm, HttpStatus.OK);
            } else {
                Object message = "No Exam instance found for generated link. ";
                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
            }
        } catch (ExamException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    "Failed to start exam: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}

package com.example.exam.controller;

import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamInstance;
import com.example.exam.service.StartExamService;
import com.example.exam.service.SubmitExamService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;


@RestController
@RequestMapping("/")
@CrossOrigin
public class SubmitExamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitExamController.class);
    private final SubmitExamService submitExamService;
    public SubmitExamController(SubmitExamService submitExamService) {
        this.submitExamService = submitExamService;
    }

    // submit exam
    @PatchMapping("/exam-gateway/{generatedLinkUrl}")
    public ResponseEntity<Object> submitExam(@PathVariable String generatedLinkUrl,
                             @RequestBody ExamInstance submittedResponse) throws IOException, ExamException {
        try{
            String message= submitExamService.updateExamInstance(generatedLinkUrl, submittedResponse);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

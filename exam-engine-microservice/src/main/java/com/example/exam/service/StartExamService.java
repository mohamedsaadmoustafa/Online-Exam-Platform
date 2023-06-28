package com.example.exam.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.example.exam.controller.StartExamController;
import com.example.exam.exception.ExamException;
import com.example.exam.util.HelperUtil;
import jakarta.persistence.EntityManager;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.exam.model.ExamInstance;
import com.example.exam.repository.ExamDefinitionRepository;
import com.example.exam.repository.ExamInstanceRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Data
@Service
public class StartExamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartExamService.class);

    private final HelperUtil helperUtil;
    private final ExamInstanceRepository examInstanceRepository;
    private final ExamDefinitionRepository examDefinitionRepository;
    private final EntityManager entityManager;

    @Value("${questions.bank.url}")
    private String questionsBankUrl;

    public StartExamService(
            ExamInstanceRepository examInstanceRepository,
            ExamDefinitionRepository examDefinitionRepository,
            HelperUtil helperUtil,
            EntityManager entityManager
    ) {
        this.examInstanceRepository = examInstanceRepository;
        this.examDefinitionRepository = examDefinitionRepository;
        this.helperUtil = helperUtil;
        this.entityManager = entityManager;
    }

    // -----------------------------  Helper Methods  -----------------------------------

    public Set<JsonNode> generateExamForm(ExamInstance examInstance) throws ExamException, IOException {
        Set<ExamInstance.Question> questions = examInstance.getQuestions();
        String questionIds = questions.stream()
                .map(ExamInstance.Question::getQuestionId)
                .collect(Collectors.joining(","));
        JsonNode questionsResponse = helperUtil.deserializeJsonResponse(questionsBankUrl + "questionsByIds?ids=" + questionIds);
        if (questionsResponse.has("error")) {
            throw new ExamException("Failed to retrieve questions: " + questionsResponse.get("error").asText());
        }
        Set<JsonNode> examForm = new HashSet<>();
        for (JsonNode question : questionsResponse) {
            if (!question.isNull()) {
                examForm.add(question);
            } else {
                throw new ExamException("Failed to retrieve question with ID: " + question.get("id").asText());
            }
        }
        examInstanceRepository.save(examInstance);
        return examForm;
    }

    // -----------------------------  Start Exam  -----------------------------------
    public Set<JsonNode> startExam(String generatedLinkUrl) throws ExamException, IOException {
        Long optionalInstanceId = helperUtil.findIdByGeneratedLinkUrl(generatedLinkUrl);
        Optional<ExamInstance> examInstanceOptional = examInstanceRepository.findById(optionalInstanceId);
        if (examInstanceOptional.isPresent()) {
            ExamInstance examInstance = examInstanceOptional.get();

            LocalDateTime scheduledTimeFrom = examInstance.getGeneratedLink().getScheduledTimeFrom();
            LocalDateTime scheduledTimeTo = examInstance.getGeneratedLink().getScheduledTimeTo();

            if (examInstance.getStatus().equals("assigned")
                    //&& scheduledTimeFrom.isAfter(LocalDateTime.now())
                    //&& scheduledTimeTo.isAfter(scheduledTimeFrom)
            ) {
                return generateExamForm(examInstance);
            } else {
                LOGGER.warn("No Exam assigned to you yet.");
                throw new ExamException("No Exam assigned to you yet.");
            }
        } else {
            LOGGER.warn("Exam Instance not found for id : " + optionalInstanceId);
            throw new ExamException("Exam Instance not found for id : " + optionalInstanceId);
        }
    }
}

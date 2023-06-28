package com.example.exam.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.exam.controller.StartExamController;
import com.example.exam.exception.ExamException;
import com.example.exam.util.HelperUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.exam.model.ExamDefinition;
import com.example.exam.model.ExamInstance;
import com.example.exam.repository.ExamDefinitionRepository;
import com.example.exam.repository.ExamInstanceRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Data
@Service
public class SubmitExamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartExamController.class);
    private final HelperUtil helperUtil;
    private final ExamInstanceRepository examInstanceRepository;
    private final ExamDefinitionRepository examDefinitionRepository;

    @Value("${questions.bank.url}")
    private String questionsBankUrl;

    public SubmitExamService(
            ExamInstanceRepository examInstanceRepository,
            ExamDefinitionRepository examDefinitionRepository,
            HelperUtil helperUtil
    ) {
        this.examInstanceRepository = examInstanceRepository;
        this.examDefinitionRepository = examDefinitionRepository;
        this.helperUtil = helperUtil;
    }

    // -----------------------------  Helper Methods  -----------------------------------

    public Float getScoreFromExamDefinition(Long examDefinitionId) {
        Optional<ExamDefinition> examDefinitionOptional = examDefinitionRepository.findById(examDefinitionId);
        if (examDefinitionOptional.isPresent()) {
            LOGGER.info("ExamDefinition with id = " + examDefinitionId + " found!");
            return examDefinitionOptional.map(ExamDefinition::getPassingScore).orElse(null);
        } else {
            LOGGER.warn("ExamDefinition with id = " + examDefinitionId + " Not found!");
            return 1.0f;
        }
    }

    public List<List<String>> getCorrectAnswersIdsForAllExam(Set<ExamInstance.Question> questions) throws ExamException, IOException {
        String questionIds = questions.stream()
                .map(ExamInstance.Question::getQuestionId)
                .collect(Collectors.joining(","));

        JsonNode questionsResponse = helperUtil.deserializeJsonResponse(questionsBankUrl + "questionsByIds-correctAnswerIds?ids=" + questionIds);
        if (questionsResponse.has("error")) {
            throw new ExamException("Failed to retrieve questions: " + questionsResponse.get("error").asText());
        }
        List<List<String>> allCorrectAnswers = new ArrayList<>();
        for (JsonNode question : questionsResponse) {
            List<String> correctAnswers = new ArrayList<>();
            JsonNode correctAnswerIds = question.get("correctAnswerIds");
            if (correctAnswerIds != null && !correctAnswerIds.isEmpty()) {
                for (JsonNode answerId : correctAnswerIds) {
                    correctAnswers.add(answerId.asText());
                }
            }
            allCorrectAnswers.add(correctAnswers);
        }
        return allCorrectAnswers;
    }

    public Float calculateScoreForQuestion(List<String> correctAnswerIds, Set<String> studentAnswerIds) {
        float score = 0F;
        List<String> studentAnswerList = new ArrayList<>(studentAnswerIds);
        for (int i = 0; i < correctAnswerIds.size() && i < studentAnswerList.size(); i++) {
            if (correctAnswerIds.contains(studentAnswerList.get(i))) {
                score++;
            }
        }
        return score / correctAnswerIds.size();
    }

    private void updateExamInstanceFields(ExamInstance existingExamInstance, ExamInstance submittedResponse) {
        existingExamInstance.setStartTime(submittedResponse.getStartTime());
        existingExamInstance.setEndTime(submittedResponse.getEndTime());
        existingExamInstance.setQuestions(submittedResponse.getQuestions());
    }

    private Float calculateStudentScore(ExamInstance existingExamInstance) throws IOException, ExamException {
        Float correctAnswers = 0F;
        Float examSize = (float) existingExamInstance.getQuestions().size();
        Set<ExamInstance.Question> studentQuestions = existingExamInstance.getQuestions();
        // to hit endpoint once to get all questions answers
        List<List<String>> correctAnswersIdsForAllExam = this.getCorrectAnswersIdsForAllExam(studentQuestions);
        int index = 0;
        for (ExamInstance.Question question : studentQuestions) {
            List<String> correctAnswerIds = correctAnswersIdsForAllExam.get(index);
            correctAnswers += calculateScoreForQuestion(correctAnswerIds, question.getSelectedAnswerIds());
            index++;
        }

        float result = correctAnswers / examSize;
        LOGGER.info("Exam Correct answers: " + correctAnswers);
        LOGGER.info("Exam result: " + result);
        return result;
    }

    private void setExamInstanceStatus(ExamInstance existingExamInstance, Float studentScore, Float passingScore) {
        if (passingScore != null && passingScore <= studentScore) {
            LOGGER.info("Exam result: Passed");
            existingExamInstance.setStatus("PASSED");
        } else {
            LOGGER.info("Exam result: Failed");
            existingExamInstance.setStatus("FAILED");
        }
    }

    // -----------------    ------------  Submit Exam  -----------------------------------
    public String updateExamInstance(String generatedLinkUrl, ExamInstance submittedResponse) throws ExamException {
        Long optionalInstanceId = helperUtil.findIdByGeneratedLinkUrl(generatedLinkUrl);
        Optional<ExamInstance> optionalExamInstance = examInstanceRepository.findById(optionalInstanceId);
        try {
            ExamInstance existingExamInstance = optionalExamInstance.orElseThrow(() -> new ExamException("Exam Instance not found for id : " + optionalInstanceId));

            if (existingExamInstance.getStatus().equals("assigned")) {
                updateExamInstanceFields(existingExamInstance, submittedResponse);
                Float studentScore = calculateStudentScore(existingExamInstance);
                Float passingScore = getScoreFromExamDefinition(existingExamInstance.getExamDefinitionId());
                setExamInstanceStatus(existingExamInstance, studentScore, passingScore);
                examInstanceRepository.save(existingExamInstance);
                LOGGER.info("Exam updated successfully.");
                // create exam summary
                helperUtil.createExamSummary(existingExamInstance, passingScore);
                return existingExamInstance.getStatus();
            } else {
                LOGGER.warn("Exam is not available anymore.");
                return "Exam is not available anymore.";
            }
        } catch (Exception e) {
            LOGGER.error("Exception:" + e.getMessage());
            throw new ExamException("An error occurred while updating the Exam Instance: " + e.getMessage());
        }
    }
}

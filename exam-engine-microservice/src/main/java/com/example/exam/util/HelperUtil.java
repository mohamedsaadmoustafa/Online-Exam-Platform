package com.example.exam.util;

import com.example.shared.model.ExamSummary;
import com.example.exam.exception.ExamException;
import com.example.exam.model.ExamInstance;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class HelperUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelperUtil.class);

    @Value("${notifications.management.microservice.url}")
    String notificationServiceURL;

    @Value("${user.microservice.url}")
    String userServiceURL;

    private final WebClient.Builder myWebClientBuilder;
    private final EntityManager entityManager;

    public HelperUtil(
            WebClient.Builder myWebClientBuilder,
            EntityManager entityManager
    ) {
        this.myWebClientBuilder = myWebClientBuilder;
        this.entityManager = entityManager;
    }

    public void postObjectToNotifications(Object objectToSend, String url) throws Exception {
        try {
            myWebClientBuilder.build()
                    .post()
                    .uri(url)
                    .bodyValue(objectToSend)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            LOGGER.info("Request sent successfully: {}", objectToSend);
        } catch (Exception e) {
            LOGGER.error("Error sending request: {}", e.getMessage());
            throw new Exception("Error sending exam summary: " + e.getMessage());
        }
    }

    public JsonNode deserializeJsonResponse(String url) throws IOException {
        WebClient webClient = WebClient.create();
        String jsonResponse = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readTree(jsonResponse);
    }

    public Long findIdByGeneratedLinkUrl(String url) throws ExamException {
        try {
            TypedQuery<ExamInstance> query = entityManager.createQuery(
                    "SELECT examInstance FROM ExamInstance examInstance WHERE examInstance.generatedLink.url = :url",
                    ExamInstance.class
            );
            query.setParameter("url", url);
            ExamInstance examInstance = query.getSingleResult();
            return examInstance.getId();
        } catch (Exception e) {
            throw new ExamException("An error occurred while finding the ID by generated link URL: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }
    public Set<ExamInstance> getAllExamInstancesForStudent(String studentId) throws ExamException {
        try {
            TypedQuery<ExamInstance> query = entityManager.createQuery(
                    "SELECT examInstance FROM ExamInstance examInstance WHERE examInstance.takenBy = :studentId",
                    ExamInstance.class
            );
            query.setParameter("studentId", studentId);
            List<ExamInstance> examInstances = query.getResultList();
            return new HashSet<>(examInstances);
        } catch (Exception e) {
            throw new ExamException("An error occurred while finding the IDs of assigned exam instances: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void createExamSummary(ExamInstance existingExamInstance, Float passingScore) throws Exception {
        ExamSummary examSummary = new ExamSummary();
        examSummary.setId(null);
        examSummary.setUserId(existingExamInstance.getTakenBy());
        examSummary.setExamInstanceId(existingExamInstance.getId());
        examSummary.setScore(passingScore);
        examSummary.setStatus(existingExamInstance.getStatus());

        String url = this.notificationServiceURL + "/receive-exam-summary";
        this.postObjectToNotifications(examSummary, url);
    }
}

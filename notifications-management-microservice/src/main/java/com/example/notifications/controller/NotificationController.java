package com.example.notifications.controller;


import com.example.notifications.DTO.ExamInstanceDTO;
import com.example.notifications.service.NotificationService;
import com.example.shared.model.ExamSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/receive-exam")
    public ResponseEntity<?> createNotification(@RequestBody ExamInstanceDTO examInstanceDTO) {
        try {
            notificationService.createNotificationExamInstance(examInstanceDTO);
            log.info("Create notification request received: {}", examInstanceDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error creating notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating notification");
        }
    }

    @PostMapping("/receive-exam-warning")
    public ResponseEntity<?> createNotificationWarning(@RequestBody ExamInstanceDTO examInstanceDTO) {
        try {
            notificationService.createNotificationExamInstanceWarning(examInstanceDTO);
            log.info("Create notification request received: {}", examInstanceDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error creating notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating notification");
        }
    }

    @PostMapping("/receive-exam-summary")
    public ResponseEntity<?> createNotificationSummary(@RequestBody ExamSummary examSummary) {
        try {
            notificationService.createNotificationSummary(examSummary);
            log.info("Exam summary request received: {}", examSummary);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error receiving exam summary: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating notification");
        }
    }

}
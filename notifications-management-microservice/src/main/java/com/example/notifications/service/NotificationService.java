package com.example.notifications.service;

import com.example.notifications.DTO.ExamInstanceDTO;
import com.example.notifications.kafka.Producer;
import com.example.notifications.model.NotificationModel;
import com.example.notifications.repository.NotificationRepository;
import com.example.shared.model.ExamSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final Producer producer;

    public NotificationService(NotificationRepository notificationRepository,
                               Producer producer
    ) {
        this.notificationRepository = notificationRepository;
        this.producer = producer;
    }

    public void createNotificationExamInstance(ExamInstanceDTO exam) {
        NotificationModel notificationModel = new NotificationModel();
        LocalDateTime notificationTime = exam.getGeneratedLink().getScheduledTimeFrom();
        if (exam.getStatus().equals("assigned")) {
            notificationModel.setMessage("You have been assigned to Exam.");
            notificationModel.setUserId(exam.getTakenBy());
            notificationModel.setNotificationTime(notificationTime);
            notificationModel.setGeneratedLinkUrl(exam.getGeneratedLink().getUrl());
            notificationModel.setNotificationType(NotificationModel.NotificationType.EXAM_NOTIFICATION);
        }

        producer.sendNotification(notificationModel);
    }

    public void createNotificationExamInstanceWarning(ExamInstanceDTO exam) {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage("You have less than 60 minutes to finish your exam.");
        notificationModel.setUserId(exam.getTakenBy());
        notificationModel.setNotificationTime(LocalDateTime.now());
        notificationModel.setGeneratedLinkUrl(exam.getGeneratedLink().getUrl());
        notificationModel.setNotificationType(NotificationModel.NotificationType.EXAM_NOTIFICATION);

        producer.sendNotificationWarning(notificationModel);
    }

    public void createNotificationSummary(ExamSummary examSummary) {

        producer.sendNotificationSummary(examSummary);
    }

    public void persistNotification(NotificationModel notificationModel) {
        notificationRepository.save(notificationModel);
        LOGGER.info("Notification saved to database {}", notificationModel);
    }

}

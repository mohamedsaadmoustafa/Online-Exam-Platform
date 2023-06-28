package com.example.notifications.kafka;

import com.example.notifications.model.NotificationModel;
import com.example.notifications.service.FirebaseMessagingService;
import com.example.notifications.service.NotificationService;
import com.example.shared.model.ExamSummary;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class Consumer {

    private final FirebaseMessagingService firebaseMessagingService;
    private final NotificationService notificationService;
    private final String topicNameEV1;
    private final String topicNameEV2;
    private final String topicNameEV3;

    public Consumer(FirebaseMessagingService firebaseMessagingService,
                    NotificationService notificationService,
                    @Value("${kafka.topics.EV1}") String topicNameEV1,
                    @Value("${kafka.topics.EV2}") String topicNameEV2,
                    @Value("${kafka.topics.EV3}") String topicNameEV3) {
        this.firebaseMessagingService = firebaseMessagingService;
        this.notificationService = notificationService;
        this.topicNameEV1 = topicNameEV1;
        this.topicNameEV2 = topicNameEV2;
        this.topicNameEV3 = topicNameEV3;
    }

    @KafkaListener(topics = "${kafka.topics.EV1}")
    public void receiveNotification(NotificationModel notificationModel) throws FirebaseMessagingException {
        this.firebaseMessagingService.sendNotification(notificationModel, "token");
        log.info("Acknowledged message from Kafka topic: {} with id: {}", this.topicNameEV1, notificationModel.getUserId());
        this.notificationService.persistNotification(notificationModel);
    }

    @KafkaListener(topics = "${kafka.topics.EV3}")
    public void receiveNotificationSummary(@Payload ExamSummary examSummary) throws FirebaseMessagingException {
        NotificationModel notificationData = new NotificationModel();
        notificationData.setMessage("Exam summary is ready.");
        notificationData.setUserId(examSummary.getUserId());
        notificationData.setNotificationTime(LocalDateTime.now());
        notificationData.setGeneratedLinkUrl("");
        notificationData.setNotificationType(NotificationModel.NotificationType.EXAM_SUMMARY);

        this.firebaseMessagingService.sendNotification(notificationData, "token");
        log.info("Acknowledged message from Kafka topic: {} with id: {}", "${kafka.topics.EV3}", examSummary.getUserId());
        this.notificationService.persistNotification(notificationData);
    }

    @KafkaListener(topics = "${kafka.topics.EV2}")
    public void receiveNotificationWarning(NotificationModel notificationModel) throws FirebaseMessagingException {
        this.firebaseMessagingService.sendNotification(notificationModel, "token");
        this.notificationService.persistNotification(notificationModel);
    }

}
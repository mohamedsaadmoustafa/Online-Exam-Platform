package com.example.notifications.kafka;

import com.example.notifications.model.NotificationModel;
import com.example.shared.model.ExamSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Producer {
    private final KafkaTemplate<String, NotificationModel> kafkaTemplate;
    private final KafkaTemplate<String, ExamSummary> examSummaryKafkaTemplate;
    private final String topicNameEV1;
    private final String topicNameEV2;
    private final String topicNameEV3;

    public Producer(KafkaTemplate<String, NotificationModel> kafkaTemplate,
                    KafkaTemplate<String, ExamSummary> examSummaryKafkaTemplate,
                    @Value("${kafka.topics.EV1}") String topicNameEV1,
                    @Value("${kafka.topics.EV2}") String topicNameEV2,
                    @Value("${kafka.topics.EV3}") String topicNameEV3) {
        this.kafkaTemplate = kafkaTemplate;
        this.examSummaryKafkaTemplate = examSummaryKafkaTemplate;
        this.topicNameEV1 = topicNameEV1;
        this.topicNameEV2 = topicNameEV2;
        this.topicNameEV3 = topicNameEV3;
    }
    // EV1
    public void sendNotification(NotificationModel notificationModel) {
        kafkaTemplate.send(this.topicNameEV1, notificationModel.getUserId(), notificationModel);
        log.info("Sent notification to Kafka topic: {} for user: {}", topicNameEV1, notificationModel.getUserId());
    }
    // EV2
    public void sendNotificationWarning(NotificationModel notificationModel) {
        kafkaTemplate.send(this.topicNameEV2, notificationModel.getUserId(), notificationModel);
        log.info("Sent Warning notification to Kafka topic: {} for user: {}", topicNameEV2, notificationModel.getUserId());
    }
    // EV3
    public void sendNotificationSummary(ExamSummary examSummary) {
        examSummaryKafkaTemplate.send(this.topicNameEV3, examSummary.getUserId(), examSummary);
        log.info("Sent exam summary to Kafka topic: {} for user: {}", topicNameEV3, examSummary.getUserId());
    }

}
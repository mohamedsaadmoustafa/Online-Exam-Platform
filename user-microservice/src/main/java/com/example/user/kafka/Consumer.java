package com.example.user.kafka;

import com.example.shared.model.ExamSummary;
import com.example.user.service.ExamSummaryService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private final ExamSummaryService examSummaryService;
    private final String topicName;

    public Consumer(ExamSummaryService examSummaryService,
                    @Value("${kafka.topic.EV3}") String topicName) {
        this.examSummaryService = examSummaryService;
        this.topicName = topicName;
    }

    @KafkaListener(topics = "${kafka.topic.EV3}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void receiveNotification(@Payload ExamSummary examSummary) {
        LOGGER.info("Acknowledged message from Kafka topic: {} with id: {}", this.topicName, examSummary.getUserId());
        this.examSummaryService.saveExamSummary(examSummary);
    }
}
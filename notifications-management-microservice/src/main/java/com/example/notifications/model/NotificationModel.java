package com.example.notifications.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class NotificationModel {
    @Id
    private String id;
    private String message;
    private String userId;
    private LocalDateTime notificationTime;
    private String generatedLinkUrl;

    private NotificationType notificationType;
    public enum NotificationType {
        EXAM_NOTIFICATION,
        EXAM_REMINDER,
        EXAM_SUMMARY
    }
}
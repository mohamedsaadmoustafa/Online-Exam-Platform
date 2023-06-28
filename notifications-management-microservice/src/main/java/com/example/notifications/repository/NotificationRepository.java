package com.example.notifications.repository;

import com.example.notifications.model.NotificationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationModel, String> {
    List<NotificationModel> findByUserId(String userId);
}
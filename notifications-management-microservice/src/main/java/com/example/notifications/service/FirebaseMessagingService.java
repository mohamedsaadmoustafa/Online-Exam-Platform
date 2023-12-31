package com.example.notifications.service;

import com.example.notifications.model.NotificationModel;
import com.example.notifications.util.HelperUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseMessagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseMessagingService.class);

    private final FirebaseMessaging firebaseMessaging;
    private final HelperUtil helperUtil;

    public FirebaseMessagingService(
            FirebaseMessaging firebaseMessaging,
            HelperUtil helperUtil) {
        this.firebaseMessaging = firebaseMessaging;
        this.helperUtil = helperUtil;
    }

    public void sendNotification(NotificationModel notificationModel) throws FirebaseMessagingException {

        String token = helperUtil.getFirebaseToken(notificationModel.getUserId());
        System.out.println(token);
        Map<String, String> myMap = new HashMap<>();

        // Adding key-value pairs to the map
        myMap.put("url", notificationModel.getGeneratedLinkUrl());
        myMap.put("userId", notificationModel.getUserId());
        myMap.put("message", notificationModel.getMessage());
        myMap.put("notificationTime", String.valueOf(notificationModel.getNotificationTime()));
        myMap.put("generatedLinkUrl", notificationModel.getGeneratedLinkUrl());
        myMap.put("notificationType", String.valueOf(notificationModel.getNotificationType()));

        Notification notification = Notification
                .builder()
                .setTitle(notificationModel.getUserId())
                .setBody(notificationModel.getMessage())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(myMap)
                .build();
        try {
            firebaseMessaging.send(message);
            LOGGER.info("Sent message to firebase");
        } catch (FirebaseMessagingException e) {
            // handle any errors that occur while sending the message
            e.printStackTrace();
        }
    }


}
package com.notificationservice.service;

import com.notificationservice.dto.NotificationRequest;
import com.notificationservice.dto.NotificationResponse;
import com.notificationservice.entity.enums.NotificationCategory;

import java.util.List;

public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest request);
    List<NotificationResponse> getNotificationsByUser(Long userId);
    List<NotificationResponse> getUnreadByUser(Long userId);
    NotificationResponse markAsRead(Long notificationId, Long userId);
    void broadcastNotification(String message, NotificationCategory category);
    void sendNotificationToUser(Long userId, String message, NotificationCategory category);
}

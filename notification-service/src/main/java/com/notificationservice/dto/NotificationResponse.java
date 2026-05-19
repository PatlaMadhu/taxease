package com.notificationservice.dto;

import com.notificationservice.entity.enums.NotificationCategory;
import com.notificationservice.entity.enums.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NotificationResponse {
    private Long notificationId;
    private Long userId;
    private Long entityId;
    private String message;
    private NotificationCategory category;
    private NotificationStatus status;
    private Instant createdDate;
}

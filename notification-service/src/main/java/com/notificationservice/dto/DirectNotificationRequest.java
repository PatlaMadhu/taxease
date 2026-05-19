package com.notificationservice.dto;

import com.notificationservice.entity.enums.NotificationCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectNotificationRequest {

    @NotBlank
    private String message;

    private NotificationCategory category;
}

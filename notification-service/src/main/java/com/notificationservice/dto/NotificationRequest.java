package com.notificationservice.dto;

import com.notificationservice.entity.enums.NotificationCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {

    @NotNull
    private Long userId;

    private Long entityId;

    @NotBlank
    private String message;

    @NotNull
    private NotificationCategory category;
}

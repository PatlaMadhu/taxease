package com.notificationservice.dto;

import com.notificationservice.entity.enums.NotificationCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BroadcastRequest {

    @NotBlank
    private String message;

    private NotificationCategory category;
}

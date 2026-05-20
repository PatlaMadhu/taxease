package com.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceUpdateEvent {
    private Long userId;
    private Long entityId;
    private String updateDetails;
}

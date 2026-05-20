package com.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilingDeadlineEvent {
    private Long userId;
    private String period;
    private String deadlineDate;
}

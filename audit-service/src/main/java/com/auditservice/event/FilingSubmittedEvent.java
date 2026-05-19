package com.auditservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilingSubmittedEvent {
    private Long filingId;
    private Long taxpayerId;
    private Long userId;
    private String taxpayerEmail;
    private String period;
    private String status;
}

package com.taxfilingservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

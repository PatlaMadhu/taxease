package com.taxpayerservice.dto.response;

import com.taxpayerservice.entity.enums.TaxpayerType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TaxpayerProfileResponse {
    private Long taxpayerId;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String taxpayerIdNumber;
    private String panNumber;
    private TaxpayerType type;
    private String address;
    private String contactInfo;
    private Instant createdAt;
}

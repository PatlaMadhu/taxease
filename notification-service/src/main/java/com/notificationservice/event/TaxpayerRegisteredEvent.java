package com.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxpayerRegisteredEvent {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String taxpayerType;
    private String address;
    private String contactInfo;
}

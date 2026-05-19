package com.identityservice.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxpayerRegistrationResponseDto {
    private Long taxpayerIdNumber;
    private String name;
    private String email;
}

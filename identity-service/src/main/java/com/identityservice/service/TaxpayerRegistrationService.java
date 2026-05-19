package com.identityservice.service;

import com.identityservice.dto.requestdto.TaxpayerRegistrationRequestDto;
import com.identityservice.dto.responsedto.TaxpayerRegistrationResponseDto;

public interface TaxpayerRegistrationService {
    TaxpayerRegistrationResponseDto registerTaxpayer(TaxpayerRegistrationRequestDto request);
}

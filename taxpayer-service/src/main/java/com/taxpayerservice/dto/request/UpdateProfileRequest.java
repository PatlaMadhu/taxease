package com.taxpayerservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 500)
    private String address;

    @Size(max = 200)
    private String contactInfo;

    @Size(max = 20)
    private String panNumber;
}

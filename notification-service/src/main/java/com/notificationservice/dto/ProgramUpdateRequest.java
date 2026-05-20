package com.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProgramUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String details;
}

package com.identityservice.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForgotPasswordRequestDto {

    @NotBlank @Email
    private String email;

    /**
     * Answer to security question: "What is your favorite place?"
     * Must match the hashed value stored during registration.
     */
    @NotBlank(message = "Security answer is required")
    private String securityAnswer;
}

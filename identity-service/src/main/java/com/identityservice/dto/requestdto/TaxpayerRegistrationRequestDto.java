package com.identityservice.dto.requestdto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.identityservice.entity.entityEnum.TaxpayerType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxpayerRegistrationRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must follow a standard format (e.g., user@example.com)")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid and contain 10 to 15 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Taxpayer type (Citizen or Business) must be specified")
    private TaxpayerType taxpayerType;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotBlank(message = "Contact info is required")
    @Size(max = 200, message = "Contact info must not exceed 200 characters")
    private String contactInfo;

    /**
     * Answer to fixed security question: "What is your favorite place?"
     * Stored as BCrypt hash. Required for account recovery via forgot-password.
     */
    @NotBlank(message = "Security answer is required (What is your favorite place?)")
    @Size(min = 2, max = 200, message = "Security answer must be between 2 and 200 characters")
    private String securityAnswer;
}

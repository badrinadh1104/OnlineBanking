package com.bankingapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class CustomerDto {

    public record CustomerRequest(
            @NotBlank(message = "First name is required") String firstName,
            @NotBlank(message = "Last name is required") String lastName,
            @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
            @NotBlank(message = "Password is required") String password,
            String phoneNumber,
            String dateOfBirth,
            String gender,
            @Valid AddressDto.AddressRequest address
    ) {}

    // Notice: password is intentionally omitted for security
    public record CustomerResponse(
            long customerId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String dateOfBirth,
            String gender,
            AddressDto.AddressResponse address,
            LocalDateTime createdDate,
            LocalDateTime updatedDate
    ) {}
}

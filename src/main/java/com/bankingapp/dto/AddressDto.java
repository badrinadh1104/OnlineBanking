package com.bankingapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class AddressDto {

    public record AddressRequest(
            @NotBlank(message = "Address line is required") String address,
            @NotBlank(message = "City is required") String city,
            @NotBlank(message = "State is required") String state,
            @NotBlank(message = "Zip code is required") String zip,
            @NotBlank(message = "Country is required") String country
    ) {}

    public record AddressResponse(
            long addressId,
            String address,
            String city,
            String state,
            String zip,
            String country,
            LocalDateTime createdDate,
            LocalDateTime updatedDate
    ) {}
}

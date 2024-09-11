package com.employee.management.app.Payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequestDTO {

    @NotNull(message = "Short code is required")
    @Size(min = 2, max = 2, message = "Short code must be exactly 2 characters")
    private String shortCode;

    @NotNull(message = "Address is required")
    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "City is required")
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @NotNull(message = "Country is required")
    @NotEmpty(message = "Country cannot be empty")
    private String country;

    @NotNull(message = "State is required")
    @NotEmpty(message = "State cannot be empty")
    private String state;

    @NotNull(message = "Pincode is required")
    @NotEmpty(message = "Pincode cannot be empty")
    private String pincode;

}

package com.employee.management.app.Payload;

import lombok.Data;

@Data
public class OrganizationResponseDTO {
    private int id;
    private String name;
    private String city;
    private String country;
    private String state;
    private String pincode;

}


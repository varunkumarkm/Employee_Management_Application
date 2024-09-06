package com.employee.management.app.Payload;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrganizationDTO {

	private int id;
    private String shortCode;
    private String address;
    private String city;
    private String country;
    private String state;
    private String pincode;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime deletedDate;
}

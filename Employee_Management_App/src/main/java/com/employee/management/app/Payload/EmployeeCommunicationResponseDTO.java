package com.employee.management.app.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCommunicationResponseDTO {

	    private Integer id;
	    private Integer employeeId;
	    private Boolean isPermanent;
	    private String address;
	    private String city;
	    private String country;
	    private String state;
	    private String pincode;
	    private String type;
	    private String value;
}

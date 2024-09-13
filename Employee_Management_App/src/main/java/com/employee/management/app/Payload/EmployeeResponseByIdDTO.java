package com.employee.management.app.Payload;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseByIdDTO {

	    private int id;
	    private String empCode;
	    private String firstName;
	    private String lastName;
	    private String fullName;
	    private String emailId;
	    private String phone;
	    private LocalDate doj;
	    private int organizationId;
	    private String organizationName;
	    private int designationId;
	    private String designationName;
	    
	    private List<EmployeeCommunicationResponseDTO> communications;
	    private List<PastExperienceRequestDTO> pastExperiences;
	    
}

package com.employee.management.app.Payload;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {

	private int id;
    private String empCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate doj;
    private int organizationId;
    private String organizationName;
    private int designationId;
    private String designationName;
}

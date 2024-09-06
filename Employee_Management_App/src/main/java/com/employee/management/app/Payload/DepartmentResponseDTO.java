package com.employee.management.app.Payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDTO  {

	private int id;
    private String name;
    private int organizationId;
    private String organizationName;
    
}

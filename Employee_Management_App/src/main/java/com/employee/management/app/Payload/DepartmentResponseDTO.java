package com.employee.management.app.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDTO  {

	private Integer id;
    private String name;
    private Integer organizationId;
    private String organizationName;
    
}

package com.employee.management.app.Service;


import org.springframework.data.domain.Page;

import com.employee.management.app.Payload.DepartmentResponseDTO;

public interface DepartmentService {


	DepartmentResponseDTO createDepartment(DepartmentResponseDTO departmentDTO);
	
	Page<DepartmentResponseDTO> getDepartments(String searchStr, Integer organizationId, int page, int pageSize);


	
}

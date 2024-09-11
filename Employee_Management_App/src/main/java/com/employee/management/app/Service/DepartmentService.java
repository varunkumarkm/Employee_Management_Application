package com.employee.management.app.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.employee.management.app.Payload.DepartmentRequestDTO;
import com.employee.management.app.Payload.DepartmentResponseDTO;


public interface DepartmentService {

	Page<DepartmentResponseDTO> getDepartments(String searchStr, Integer organizationId, int page, int pageSize, Sort sort);
	
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest);
	
}

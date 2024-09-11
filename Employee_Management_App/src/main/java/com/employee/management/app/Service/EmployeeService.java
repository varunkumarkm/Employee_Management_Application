package com.employee.management.app.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseDTO;

public interface EmployeeService {

    EmployeeRequestDTO createEmployee(EmployeeRequestDTO employeeDto);
    
    Page<EmployeeResponseDTO> getEmployees(String searchStr, Integer organizationId, Integer designationId, String doj, int page, int pageSize, Sort sort);

	
}

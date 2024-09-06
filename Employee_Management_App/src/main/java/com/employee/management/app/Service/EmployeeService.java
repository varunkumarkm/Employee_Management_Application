package com.employee.management.app.Service;

import java.time.LocalDate;
import com.employee.management.app.Payload.EmployeeDTO;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseDTO;

public interface EmployeeService {

	EmployeeResponseDTO getEmployees(String searchStr, Integer organizationId, Integer designationId, LocalDate doj, int page, int pageSize);
    EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);
}

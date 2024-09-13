package com.employee.management.app.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.employee.management.app.Exception.ResourceNotFoundException;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseByIdDTO;

public interface EmployeeService {

    EmployeeRequestDTO createEmployee(EmployeeRequestDTO employeeDto);
    
    Page<EmployeeResponseByIdDTO> getEmployees(String searchStr, Integer organizationId, Integer designationId, String doj, int page, int pageSize, Sort sort);

    EmployeeResponseByIdDTO getEmployeeById(Integer employeeId) throws ResourceNotFoundException;

}

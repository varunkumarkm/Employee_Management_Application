package com.employee.management.app.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Designation;
import com.employee.management.app.Entities.Employee;
import com.employee.management.app.Entities.Organization;
import com.employee.management.app.Payload.EmployeeDTO;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseDTO;
import com.employee.management.app.Repository.DesignationRepository;
import com.employee.management.app.Repository.EmployeeRepository;
import com.employee.management.app.Repository.OrganizationRepository;
import com.employee.management.app.Service.EmployeeService;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private DesignationRepository designationRepository;

    @Override
    public EmployeeResponseDTO getEmployees(String searchStr, Integer organizationId, Integer designationId, LocalDate doj, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Employee> employeePage = employeeRepository.findEmployees(searchStr, organizationId, designationId, doj, pageable);

        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setTotal((int) employeePage.getTotalElements());
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setData(employeePage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));

        return response;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = convertToEntity(employeeRequestDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmpCode(employee.getEmpCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFullName());
        dto.setEmailId(employee.getEmailId());
        dto.setPhone(employee.getPhone());
        dto.setDoj(employee.getDoj());

        if (employee.getOrganization() != null) {
            dto.setOrganizationId(employee.getOrganization().getId());
            dto.setOrganizationName(employee.getOrganization().getShortCode());  
        }

        if (employee.getDesignation() != null) {
            dto.setDesignationId(employee.getDesignation().getId());
            dto.setDesignationName(employee.getDesignation().getName()); 
        }

        return dto;
    }

	private Employee convertToEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setEmpCode(dto.getEmpCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFullName());
        employee.setEmailId(dto.getEmailId());
        employee.setPhone(dto.getPhone());
        employee.setDoj(dto.getDoj());

        Organization organization = organizationRepository.findById(dto.getOrganizationId()).orElse(null);
        Designation designation = designationRepository.findById(dto.getDesignationId()).orElse(null);

        employee.setOrganization(organization);
        employee.setDesignation(designation);

        return employee;
    }
}


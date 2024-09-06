package com.employee.management.app.ServiceImpl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Department;
import com.employee.management.app.Entities.Organization;
import com.employee.management.app.Exception.ResourceNotFoundException;
import com.employee.management.app.Payload.DepartmentResponseDTO;
import com.employee.management.app.Repository.DepartmentRepository;
import com.employee.management.app.Repository.OrganizationRepository;
import com.employee.management.app.Service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	 @Autowired
	    private DepartmentRepository departmentRepository;
	 
	 @Autowired
	    private OrganizationRepository organizationRepository;
	 
	 
	 @Override
	 public DepartmentResponseDTO createDepartment(DepartmentResponseDTO departmentDTO) {
		    Department department = new Department();
		    department.setName(departmentDTO.getName());
		    
		    department.setCreatedDate(LocalDateTime.now());
		    department.setModifiedDate(LocalDateTime.now());
		    department.setDeletedDate(LocalDateTime.now());

		    Organization organization = organizationRepository.findById(departmentDTO.getOrganizationId())
		    		.orElseThrow(
		    	    		()-> new ResourceNotFoundException("Organization", "id", departmentDTO.getOrganizationId())
		    	    		);
		    department.setOrganization(organization);

		    Department savedDepartment = departmentRepository.save(department);

		    DepartmentResponseDTO savedDepartmentDTO = new DepartmentResponseDTO();
		    savedDepartmentDTO.setId(savedDepartment.getId());
		    savedDepartmentDTO.setName(savedDepartment.getName());
		    savedDepartmentDTO.setOrganizationId(savedDepartment.getOrganization().getId());
		    savedDepartmentDTO.setOrganizationName(savedDepartment.getOrganization().getShortCode());

		    return savedDepartmentDTO;
		}


	 @Override
	 public Page<DepartmentResponseDTO> getDepartments(String searchStr, Integer organizationId, int page, int pageSize) {
	     Pageable pageable = PageRequest.of(page - 1, pageSize);
	     Page<Department> departments;

	     if (searchStr != null && organizationId != null) {
	         departments = departmentRepository.findByNameContainingAndOrganizationId(searchStr, organizationId, pageable);
	     } else if (searchStr != null) {
	         departments = departmentRepository.findByNameContaining(searchStr, pageable);
	     } else if (organizationId != null) {
	         departments = departmentRepository.findByOrganizationId(organizationId, pageable);
	     } else {
	         departments = departmentRepository.findAll(pageable);
	     }

	     return departments.map(department -> {
	         DepartmentResponseDTO dto = new DepartmentResponseDTO();
	         dto.setId(department.getId());
	         dto.setName(department.getName());
	         dto.setOrganizationId(department.getOrganization().getId());
	         dto.setOrganizationName(department.getOrganization().getShortCode());
	         return dto;
	     });
	 }
}

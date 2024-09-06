package com.employee.management.app.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.employee.management.app.Payload.OrganizationDTO;
import com.employee.management.app.Payload.OrganizationResponseDTO;

public interface OrganizationService {

	OrganizationDTO createOrganization(OrganizationDTO organizationDto);

	Page<OrganizationResponseDTO> getOrganizations(String searchStr, Pageable pageable);

}

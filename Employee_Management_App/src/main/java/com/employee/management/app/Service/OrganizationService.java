package com.employee.management.app.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.employee.management.app.Payload.OrganizationRequestDTO;
import com.employee.management.app.Payload.OrganizationResponseDTO;

public interface OrganizationService {

	OrganizationRequestDTO createOrganization(OrganizationRequestDTO organizationDto);

	Page<OrganizationResponseDTO> getOrganizations(String searchStr, int page, int pageSize, Sort sort);

}

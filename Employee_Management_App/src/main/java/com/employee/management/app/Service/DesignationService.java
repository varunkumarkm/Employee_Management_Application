package com.employee.management.app.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.employee.management.app.Payload.DesignationResponseDTO;

	public interface DesignationService {
		
		DesignationResponseDTO createDesignation(DesignationResponseDTO designationDTO);

		Page<DesignationResponseDTO> getDesignations(String searchStr, int page, int pageSize, Sort sort);
	
	}

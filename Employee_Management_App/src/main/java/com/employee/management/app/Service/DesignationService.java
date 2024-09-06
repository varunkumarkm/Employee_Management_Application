package com.employee.management.app.Service;

import org.springframework.data.domain.Page;
import com.employee.management.app.Payload.DesignationDTO;

public interface DesignationService {

	Page<DesignationDTO> getDesignations(String searchStr, int page, int pageSize);

	DesignationDTO createDesignation(DesignationDTO designationDTO);

}

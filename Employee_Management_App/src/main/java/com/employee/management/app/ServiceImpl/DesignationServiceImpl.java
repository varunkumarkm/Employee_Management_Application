package com.employee.management.app.ServiceImpl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Designation;
import com.employee.management.app.Payload.DesignationDTO;
import com.employee.management.app.Repository.DesignationRepository;
import com.employee.management.app.Service.DesignationService;

@Service
public class DesignationServiceImpl implements DesignationService {

	
	@Autowired
	private DesignationRepository designationRepository;
	
	@Override
	 public DesignationDTO createDesignation(DesignationDTO designationDTO) {
        Designation designation = new Designation();
        designation.setName(designationDTO.getName());
        designation.setCreatedDate(LocalDateTime.now());
        designation = designationRepository.save(designation);
        return mapToDTO(designation);
    }
	
	@Override
    public Page<DesignationDTO> getDesignations(String searchStr, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Designation> designations;
        if (searchStr == null || searchStr.isEmpty()) {
            designations = designationRepository.findAll(pageable);
        } else {
            designations = designationRepository.findByNameContainingIgnoreCase(searchStr, pageable);
        }

        return designations.map(this::mapToDTO);
    }
    private DesignationDTO mapToDTO(Designation designation) {
        DesignationDTO dto = new DesignationDTO();
        dto.setId(designation.getId());
        dto.setName(designation.getName());
        return dto;
    }
}


package com.employee.management.app.ServiceImpl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Organization;
import com.employee.management.app.Payload.OrganizationDTO;
import com.employee.management.app.Payload.OrganizationResponseDTO;
import com.employee.management.app.Repository.OrganizationRepository;
import com.employee.management.app.Service.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
    private OrganizationRepository organizationRepository;
	
	@Override
	public OrganizationDTO createOrganization(OrganizationDTO organizationDto) {
		
		 Organization organization = new Organization();
		    organization.setShortCode(organizationDto.getShortCode());
		    organization.setAddress(organizationDto.getAddress());
		    organization.setCity(organizationDto.getCity());
		    organization.setCountry(organizationDto.getCountry());
		    organization.setState(organizationDto.getState());
		    organization.setPincode(organizationDto.getPincode());
		    
		    LocalDateTime now = LocalDateTime.now();
		    organization.setCreatedDate(now);
		    organization.setModifiedDate(now);
		    
		    Organization savedOrganization = organizationRepository.save(organization);
		    
		    return convertToDto(savedOrganization);
		}

		private OrganizationDTO convertToDto(Organization organization) {
		    OrganizationDTO dto = new OrganizationDTO();
		    dto.setId(organization.getId()); 
		    dto.setShortCode(organization.getShortCode());
		    dto.setAddress(organization.getAddress());
		    dto.setCity(organization.getCity());
		    dto.setCountry(organization.getCountry());
		    dto.setState(organization.getState());
		    dto.setPincode(organization.getPincode());
		    dto.setCreatedDate(organization.getCreatedDate());
		    dto.setModifiedDate(organization.getModifiedDate());
		    dto.setDeletedDate(organization.getDeletedDate());
		    return dto;
		}

		@Override
		public Page<OrganizationResponseDTO> getOrganizations(String searchStr, Pageable pageable) {
		    Page<Organization> organizationsPage;
		    
		    if (searchStr != null && !searchStr.isEmpty()) {
		        organizationsPage = organizationRepository.findByShortCodeContainingIgnoreCase(searchStr, pageable);
		    } else {
		        organizationsPage = organizationRepository.findAll(pageable);
		    }

		    return organizationsPage.map(this::convertToDTO);
		}

		private OrganizationResponseDTO convertToDTO(Organization organization) {
		    OrganizationResponseDTO dto = new OrganizationResponseDTO();
		    dto.setId(organization.getId());
		    dto.setShortCode(organization.getShortCode());
		    dto.setCity(organization.getCity());
		    dto.setCountry(organization.getCountry());
		    dto.setState(organization.getState());
		    dto.setPincode(organization.getPincode());
		    return dto;
		}
}











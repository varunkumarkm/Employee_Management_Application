package com.employee.management.app.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.employee.management.app.Payload.PastExperienceRequestDTO;
import com.employee.management.app.Payload.PastExperienceResponseDTO;

public interface PastExperienceService {

	PastExperienceRequestDTO createPastExperience(PastExperienceRequestDTO pastExperienceDTO);

//    Page<PastExperienceResponseDTO> getAllPastExperiences(String searchStr, String companyName, String jobTitle,
//			Pageable pageable);


	
}

package com.employee.management.app.Controller;

import com.employee.management.app.Payload.PastExperienceRequestDTO;
import com.employee.management.app.Payload.PastExperienceResponseDTO;
import com.employee.management.app.Service.PastExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/past-experiences")
public class PastExperienceController {

    @Autowired
    private PastExperienceService pastExperienceService;

    @PostMapping
    public ResponseEntity<PastExperienceRequestDTO> createPastExperience(@RequestBody PastExperienceRequestDTO pastExperienceDTO) {
    	PastExperienceRequestDTO createdExperience = pastExperienceService.createPastExperience(pastExperienceDTO);
        return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<Page<PastExperienceResponseDTO>> getAllPastExperiences(
//            @RequestParam(value = "searchStr", required = false, defaultValue = "") String searchStr,
//            @RequestParam(value = "companyName", required = false) String companyName,
//            @RequestParam(value = "jobTitle", required = false) String jobTitle,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//            @RequestParam(value = "sort", defaultValue = "id,ASC") String sort) {
//
//        // Handle sorting
//        String[] sortParams = sort.split(",");
//        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);
//        String sortBy = sortParams[0];
//        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortDirection, sortBy));
//
//        Page<PastExperienceResponseDTO> experiences = pastExperienceService.getAllPastExperiences(
//                searchStr, companyName, jobTitle, pageable);
//
//        return ResponseEntity.ok(experiences);
//    }
}
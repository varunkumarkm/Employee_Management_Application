package com.employee.management.app.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.PastExperience;
import com.employee.management.app.Exception.ResourceNotFoundException;
import com.employee.management.app.Payload.PastExperienceRequestDTO;
import com.employee.management.app.Payload.PastExperienceResponseDTO;
import com.employee.management.app.Repository.PastExperienceRepository;
import com.employee.management.app.Service.PastExperienceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PastExperienceServiceImpl implements PastExperienceService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(PastExperienceServiceImpl.class);

    @Autowired
    private PastExperienceRepository pastExperienceRepository;
    
    @Autowired
    private ModelMapper modelMapper; 
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public PastExperienceRequestDTO createPastExperience(PastExperienceRequestDTO pastExperienceDTO) {
        logger.info("Creating Past Experience with Designation: {}", pastExperienceDTO.getDesignation());

        if (pastExperienceDTO.getDesignation() == null) {
            logger.error("Designation is null in the request");
            throw new ResourceNotFoundException("Designation cannot be null");
        }

        if (pastExperienceDTO.getDesignation().getDesignationName() == null || pastExperienceDTO.getDesignation().getDesignationName().isEmpty()) {
            throw new ResourceNotFoundException("Designation name cannot be null or empty");
        }

        if (pastExperienceDTO.getCompanyName() == null || pastExperienceDTO.getCompanyName().isEmpty()) {
            throw new ResourceNotFoundException("Company name cannot be null or empty");
        }

        if (pastExperienceDTO.getStartDate() != null && pastExperienceDTO.getEndDate() != null
            && pastExperienceDTO.getStartDate().isAfter(pastExperienceDTO.getEndDate())) {
            throw new ResourceNotFoundException("Start date cannot be after end date");
        }

        PastExperience pastExperience;
        try {
            pastExperience = modelMapper.map(pastExperienceDTO, PastExperience.class);
        } catch (MappingException ex) {
            logger.error("Error occurred while mapping PastExperienceDTO to PastExperience: {}", ex.getMessage());
            throw new RuntimeException("Data mapping error");
        }

        PastExperience savedExperience = pastExperienceRepository.save(pastExperience);
        logger.info("Past Experience created with ID: {}", savedExperience.getId());

        return modelMapper.map(savedExperience, PastExperienceRequestDTO.class);
    }

//    @Override
//    public Page<PastExperienceResponseDTO> getAllPastExperiences(String searchStr, String companyName, String jobTitle, Pageable pageable) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<PastExperience> cq = cb.createQuery(PastExperience.class);
//        Root<PastExperience> root = cq.from(PastExperience.class);
//
//        // Build predicates based on search filters
//        Predicate predicate = cb.conjunction();
//        if (searchStr != null && !searchStr.isEmpty()) {
//            predicate = cb.and(predicate, cb.or(
//                cb.like(cb.lower(root.get("companyName")), "%" + searchStr.toLowerCase() + "%"),
//                cb.like(cb.lower(root.get("designation").get("designationName")), "%" + searchStr.toLowerCase() + "%")
//            ));
//        }
//        if (companyName != null && !companyName.isEmpty()) {
//            predicate = cb.and(predicate, cb.equal(cb.lower(root.get("companyName")), companyName.toLowerCase()));
//        }
//        if (jobTitle != null && !jobTitle.isEmpty()) {
//            predicate = cb.and(predicate, cb.equal(cb.lower(root.get("designation").get("designationName")), jobTitle.toLowerCase()));
//        }
//
//        cq.where(predicate);
//
//        // Execute the query with pagination
//        TypedQuery<PastExperience> query = entityManager.createQuery(cq);
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        List<PastExperience> pastExperiences = query.getResultList();
//        logger.debug("Fetched Past Experiences: {}", pastExperiences);
//        
//        // Map the entity list to DTO list
//        List<PastExperienceResponseDTO> pastExperienceDTOs = pastExperiences.stream()
//            .map(this::mapToResponseDTO)
//            .collect(Collectors.toList());
//
//        // Get the total count
//        long totalRecords = countPastExperiences(predicate);
//        logger.debug("Total Records Count: {}", totalRecords);
//
//        // Return a page of PastExperienceResponseDTOs
//        return new PageImpl<>(pastExperienceDTOs, pageable, totalRecords);
//    }
//
//    private long countPastExperiences(Predicate predicate) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        Root<PastExperience> root = countQuery.from(PastExperience.class); // Use PastExperience entity
//        countQuery.select(cb.count(root)).where(predicate);
//
//        TypedQuery<Long> query = entityManager.createQuery(countQuery);
//        return query.getMaxResults();
//    }
//
//    private PastExperienceResponseDTO mapToResponseDTO(PastExperience experience) {
//        // Assuming ModelMapper is configured
//        return modelMapper.map(experience, PastExperienceResponseDTO.class);
//    }
}
package com.employee.management.app.Repository;

import com.employee.management.app.Entities.PastExperience;
import org.springframework.data.domain.Page;  // Correct import for Page
import org.springframework.data.domain.Pageable;  // Correct import for Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastExperienceRepository extends JpaRepository<PastExperience, Integer> {

	//Page<PastExperience> findByCompanyNameContainingIgnoreCase(String searchStr, Pageable pageable);

}

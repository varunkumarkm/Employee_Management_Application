package com.employee.management.app.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
	
	
		boolean existsByName(String name);
	
		boolean existsByShortCode(String shortCode);
	
		Page<Organization> findAllByNameContainingIgnoreCase(String searchStr, Pageable pageable);


		
}
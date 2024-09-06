package com.employee.management.app.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

	Page<Designation> findByNameContainingIgnoreCase(String searchStr, Pageable pageable);
}

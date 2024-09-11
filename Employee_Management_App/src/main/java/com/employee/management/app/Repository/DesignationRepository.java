package com.employee.management.app.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

	 @Query("SELECT d FROM Designation d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :searchStr, '%'))")
	    Page<Designation> findByNameContainingIgnoreCase(@Param("searchStr") String searchStr, Pageable pageable);
}

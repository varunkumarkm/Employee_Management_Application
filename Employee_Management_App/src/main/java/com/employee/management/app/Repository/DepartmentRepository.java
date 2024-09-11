package com.employee.management.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Department;
import com.employee.management.app.Entities.Organization;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	@Query("SELECT o FROM Organization o WHERE o.id = :organizationId")
    Organization findOrganizationById(@Param("organizationId") Integer organizationId);
    	
}

package com.employee.management.app.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	Page<Department> findByNameContaining(String name, Pageable pageable);

    Page<Department> findByNameContainingAndOrganizationId(String name, Integer organizationId, Pageable pageable);

    Page<Department> findByOrganizationId(Integer organizationId, Pageable pageable);
    	
}

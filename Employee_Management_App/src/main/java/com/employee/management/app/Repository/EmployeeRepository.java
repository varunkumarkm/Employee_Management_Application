package com.employee.management.app.Repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	
	@Query("SELECT e FROM Employee e WHERE (:searchStr IS NULL OR e.fullName LIKE %:searchStr%) AND (:organizationId IS NULL OR e.organization.id = :organizationId) AND (:designationId IS NULL OR e.designation.id = :designationId) AND (:doj IS NULL OR e.doj = :doj)")
	Page<Employee> findEmployees(
	    @Param("searchStr") String searchStr,
	    @Param("organizationId") Integer organizationId,
	    @Param("designationId") Integer designationId,
	    @Param("doj") LocalDate doj,
	    Pageable pageable
	);


	}

package com.employee.management.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	boolean existsByEmpCode(String empCode);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	}

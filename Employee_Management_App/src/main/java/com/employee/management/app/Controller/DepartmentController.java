package com.employee.management.app.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.employee.management.app.Payload.DepartmentResponseDTO;
import com.employee.management.app.Service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	@Autowired
    private DepartmentService departmentService;
	
	
	@PostMapping
	 public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentResponseDTO departmentRequest) {
	    DepartmentResponseDTO department = departmentService.createDepartment(departmentRequest);
	    return ResponseEntity.status(HttpStatus.CREATED).body(department);
	}
	
	 @GetMapping
	 //localhost:8080/api/?page=1&pageSize=10
	 public ResponseEntity<Map<String, Object>> getDepartments(
	            @RequestParam(value = "searchStr", required = false) String searchStr,
	            @RequestParam(value = "organizationId", required = false) Integer organizationId,
	            @RequestParam(value = "page", defaultValue = "1") int page,
	            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

	        Page<DepartmentResponseDTO> departments = departmentService.getDepartments(searchStr, organizationId, page, pageSize);

	        Map<String, Object> response = new HashMap<>();
	        response.put("data", departments.getContent());
	        response.put("currentPage", departments.getNumber() + 1);
	        response.put("totalItems", departments.getTotalElements());
	        response.put("totalPages", departments.getTotalPages());

	        return ResponseEntity.ok(response);
	    }
	}

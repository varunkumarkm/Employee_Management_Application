package com.employee.management.app.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseDTO;
import com.employee.management.app.Service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	  @Autowired
	    private EmployeeService employeeService;
	    
	  @PostMapping
	    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDto, BindingResult result) {
	        if (result.hasErrors()) {
	            Map<String, String> validationErrors = new HashMap<>();
	            result.getFieldErrors().forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
	        }
	        try {
	            EmployeeRequestDTO createdEmployee = employeeService.createEmployee(employeeRequestDto);
	            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
	        } catch (Exception e) {
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", "Unable to create employee: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	        }
	  }

	  @GetMapping
	    public ResponseEntity<Map<String, Object>> getEmployees(
	            @RequestParam(value = "searchStr", required = false) String searchStr,
	            @RequestParam(value = "organizationId", required = false) Integer organizationId,
	            @RequestParam(value = "designationId", required = false) Integer designationId,
	            @RequestParam(value = "doj", required = false) String doj,
	            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
	            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) @Max(30) int pageSize,
	            @RequestParam(value = "sort", required = false) String sortString) {

	        try {
	            Sort sort = Sort.by(Sort.Order.asc("firstName"));
	            if (sortString != null && !sortString.isEmpty()) {
	                String[] sortParams = sortString.split(",");
	                if (sortParams.length == 2) {
	                    String sortField = sortParams[0];
	                    Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);
	                    if (sortField.equals("firstName") || sortField.equals("lastName") || sortField.equals("empCode") || sortField.equals("fullName")) {
	                        sort = Sort.by(Sort.Order.by(sortField).with(sortDirection));
	                    }
	                }
	            }

	            Page<EmployeeResponseDTO> employeePage = employeeService.getEmployees(searchStr, organizationId, designationId, doj, page, pageSize, sort);

	            Map<String, Object> response = new HashMap<>();
	            response.put("data", employeePage.getContent());
	            response.put("currentPage", employeePage.getNumber() + 1);
	            response.put("totalItems", employeePage.getTotalElements());
	            response.put("totalPages", employeePage.getTotalPages());
	            response.put("pageSize", employeePage.getSize());
	            response.put("isFirst", employeePage.isFirst());
	            response.put("isLast", employeePage.isLast());
	            response.put("hasNext", employeePage.hasNext());
	            response.put("hasPrevious", employeePage.hasPrevious());
	            response.put("sort", employeePage.getSort());
	            response.put("numberOfElements", employeePage.getNumberOfElements());
	            response.put("isEmpty", employeePage.isEmpty());

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            Map<String, Object> errorResponse = new HashMap<>();
	            errorResponse.put("error", "Unable to fetch employees: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	        }
	    }
	}
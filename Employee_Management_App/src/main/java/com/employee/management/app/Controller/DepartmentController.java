package com.employee.management.app.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.employee.management.app.Payload.DepartmentRequestDTO;
import com.employee.management.app.Payload.DepartmentResponseDTO;
import com.employee.management.app.Service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDepartments(
            @RequestParam(value = "searchStr", required = false) String searchStr,
            @RequestParam(value = "organizationId", required = false) Integer organizationId,
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) @Max(30) int pageSize,
            @RequestParam(value = "sort", required = false) String sortString) {

        try {
            // Convert sortString to Sort object
            Sort sort = Sort.by(Sort.Order.asc("name")); // Default sort if none provided
            if (sortString != null && !sortString.isEmpty()) {
                String[] sortParams = sortString.split(",");
                if (sortParams.length == 2) {
                    sort = Sort.by(Sort.Order.by(sortParams[0]).with(Sort.Direction.fromString(sortParams[1])));
                }
            }

            // Fetch paginated departments
            Page<DepartmentResponseDTO> departmentPage = departmentService.getDepartments(searchStr, organizationId, page, pageSize, sort);

            // Prepare the response map
            Map<String, Object> response = new HashMap<>();
            response.put("data", departmentPage.getContent());
            response.put("currentPage", departmentPage.getNumber() + 1);  // Adjust for 0-based page number
            response.put("totalItems", departmentPage.getTotalElements());
            response.put("totalPages", departmentPage.getTotalPages());
            response.put("pageSize", departmentPage.getSize());
            response.put("isFirst", departmentPage.isFirst());
            response.put("isLast", departmentPage.isLast());
            response.put("hasNext", departmentPage.hasNext());
            response.put("hasPrevious", departmentPage.hasPrevious());
            response.put("sort", departmentPage.getSort());
            response.put("numberOfElements", departmentPage.getNumberOfElements());
            response.put("isEmpty", departmentPage.isEmpty());

            // Return the response entity with the correct map
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle any exceptions and return an error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to fetch departments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequest) {
        try {
            DepartmentResponseDTO createdDepartment = departmentService.createDepartment(departmentRequest);
            // Return the created department response with status 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (Exception e) {
            // Create error response map
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to create department: " + e.getMessage());
            // Return error response with status 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
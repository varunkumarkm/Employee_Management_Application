package com.employee.management.app.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.employee.management.app.Payload.DesignationResponseDTO;
import com.employee.management.app.Service.DesignationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/designations")
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    // Get Designations with search, pagination, and sort
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDesignations(
            @RequestParam(value = "searchStr", required = false) String searchStr,
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) @Max(30) int pageSize,
            @RequestParam(value = "sort", required = false) String sortString) {

        try {
            // Sorting mechanism
            Sort sort = Sort.by(Sort.Order.asc("name")); // Default sort by name
            if (sortString != null && !sortString.isEmpty()) {
                String[] sortParams = sortString.split(",");
                if (sortParams.length == 2) {
                    sort = Sort.by(Sort.Order.by(sortParams[0]).with(Sort.Direction.fromString(sortParams[1])));
                }
            }

            // Fetch paginated designations
            Page<DesignationResponseDTO> designationPage = designationService.getDesignations(searchStr, page, pageSize, sort);

            // Prepare the response map
            Map<String, Object> response = new HashMap<>();
            response.put("data", designationPage.getContent());
            response.put("currentPage", designationPage.getNumber() + 1);  // Adjust for 0-based page number
            response.put("totalItems", designationPage.getTotalElements());
            response.put("totalPages", designationPage.getTotalPages());
            response.put("pageSize", designationPage.getSize());
            response.put("isFirst", designationPage.isFirst());
            response.put("isLast", designationPage.isLast());
            response.put("hasNext", designationPage.hasNext());
            response.put("hasPrevious", designationPage.hasPrevious());
            response.put("sort", designationPage.getSort());
            response.put("numberOfElements", designationPage.getNumberOfElements());
            response.put("isEmpty", designationPage.isEmpty());

            // Return OK response
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle exceptions
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to fetch designations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createDesignation(@Valid @RequestBody DesignationResponseDTO designationRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> validationErrors = new HashMap<>();
            result.getFieldErrors().forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
        }
        try {
            DesignationResponseDTO createdDesignation = designationService.createDesignation(designationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDesignation);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to create designation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

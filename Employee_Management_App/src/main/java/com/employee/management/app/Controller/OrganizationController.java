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
import com.employee.management.app.Payload.OrganizationRequestDTO;
import com.employee.management.app.Payload.OrganizationResponseDTO;
import com.employee.management.app.Service.OrganizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    
    
    @PostMapping
    public ResponseEntity<OrganizationRequestDTO> createOrganization(@Valid @RequestBody OrganizationRequestDTO organizationDto) {
           return new ResponseEntity<OrganizationRequestDTO>(organizationService.createOrganization(organizationDto), HttpStatus.CREATED);
       }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrganizations(
            @RequestParam(value = "searchStr", required = false) String searchStr,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "name,asc") String[] sort) {

        try {
            // Extract sort field and direction
            String sortField = sort[0];
            String sortDirection = sort[1];

            // Prepare Sort object
            Sort sorting = Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), sortField);

            // Fetch paginated organizations with sorting
            Page<OrganizationResponseDTO> organizationPage = organizationService.getOrganizations(searchStr, page, pageSize, sorting);

            // Prepare the response map
            Map<String, Object> response = new HashMap<>();
            response.put("data", organizationPage.getContent());
            response.put("currentPage", organizationPage.getNumber() + 1);
            response.put("totalItems", organizationPage.getTotalElements());
            response.put("totalPages", organizationPage.getTotalPages());
            response.put("pageSize", organizationPage.getSize());
            response.put("isFirst", organizationPage.isFirst());
            response.put("isLast", organizationPage.isLast());
            response.put("hasNext", organizationPage.hasNext());
            response.put("hasPrevious", organizationPage.hasPrevious());
            response.put("sort", organizationPage.getSort());
            response.put("numberOfElements", organizationPage.getNumberOfElements());
            response.put("isEmpty", organizationPage.isEmpty());

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            
            // Handle any exceptions and return an error response
        	Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("Unable to fetch organizations: " + e.getMessage(), errorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

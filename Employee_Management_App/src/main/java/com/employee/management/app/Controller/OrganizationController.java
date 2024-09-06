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
import com.employee.management.app.Payload.OrganizationDTO;
import com.employee.management.app.Payload.OrganizationResponseDTO;
import com.employee.management.app.Service.OrganizationService;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    
    
    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDto) {
           return new ResponseEntity<OrganizationDTO>(organizationService.createOrganization(organizationDto), HttpStatus.CREATED);
       }

    @GetMapping
    //localhost:8080/api/organizations?page=1&pageSize=05
    public ResponseEntity<Map<String, Object>> getOrganizations(
            @RequestParam(value = "searchStr", required = false) String searchStr,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<OrganizationResponseDTO> organizations = organizationService.getOrganizations(searchStr, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", organizations.getTotalElements());
        response.put("data", organizations.getContent());
        response.put("totalPages", organizations.getTotalPages());
        response.put("currentPage", organizations.getNumber() + 1);

        return ResponseEntity.ok(response);
    }
}



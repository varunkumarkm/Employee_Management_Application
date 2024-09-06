package com.employee.management.app.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.employee.management.app.Payload.DesignationDTO;
import com.employee.management.app.Service.DesignationService;

@RestController
@RequestMapping("/api/designations")
public class DesignationController {

    @Autowired
    private DesignationService designationService;
    
    
    @PostMapping
    public ResponseEntity<DesignationDTO> createDesignation(@RequestBody DesignationDTO designationDTO) {
        DesignationDTO createdDesignation = designationService.createDesignation(designationDTO);
        return new ResponseEntity<>(createdDesignation, HttpStatus.CREATED);
    }

    @GetMapping
    //GET /api/designations?searchStr=Developer&page=1&pageSize=10
    public ResponseEntity<Map<String, Object>> getDesignations(
            @RequestParam(required = false) String searchStr,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Page<DesignationDTO> designationPage = designationService.getDesignations(searchStr, page, pageSize);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", designationPage.getContent());
        response.put("currentPage", designationPage.getNumber() + 1);
        response.put("totalItems", designationPage.getTotalElements());
        response.put("totalPages", designationPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
}


package com.employee.management.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.employee.management.app.Payload.EmployeeCommunicationRequestDTO;
import com.employee.management.app.Payload.EmployeeCommunicationResponseDTO;
import com.employee.management.app.Service.EmployeeCommunicationService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/employee-communications")
public class EmployeeCommunicationController {

    @Autowired
    private EmployeeCommunicationService employeeCommunicationService;

    @GetMapping
    public ResponseEntity<Page<EmployeeCommunicationResponseDTO>> getEmployeeCommunications(
            @RequestParam(value = "searchStr", required = false) String searchStr,
            @RequestParam(value = "employeeId", required = false) Integer employeeId,
            @RequestParam(value = "isPermanent", required = false) Boolean isPermanent,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {

        // Dynamic sorting direction
        Sort sorting = "desc".equalsIgnoreCase(sortDirection) ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Page<EmployeeCommunicationResponseDTO> responseDTOs = employeeCommunicationService.getEmployeeCommunications(
                searchStr, employeeId, isPermanent, page, pageSize, sorting);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeCommunicationResponseDTO> createEmployeeCommunication(
            @Valid @RequestBody EmployeeCommunicationRequestDTO requestDTO) {
        EmployeeCommunicationResponseDTO responseDTO = employeeCommunicationService.createEmployeeCommunication(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED); 
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeCommunicationResponseDTO> updateEmployeeCommunication(
            @PathVariable("id") Integer id,
            @Valid @RequestBody EmployeeCommunicationRequestDTO requestDTO) {
        EmployeeCommunicationResponseDTO responseDTO = employeeCommunicationService.updateEmployeeCommunication(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeCommunication(@PathVariable("id") Integer id) {
        employeeCommunicationService.deleteEmployeeCommunication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }
}

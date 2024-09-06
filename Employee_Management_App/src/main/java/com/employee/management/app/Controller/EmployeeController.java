package com.employee.management.app.Controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.employee.management.app.Payload.EmployeeDTO;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseDTO;
import com.employee.management.app.Service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    //localhost:8080/api/employees?page=1&pageSize=05
    public EmployeeResponseDTO getEmployees(
            @RequestParam(required = false) String searchStr,
            @RequestParam(required = false) Integer organizationId,
            @RequestParam(required = false) Integer designationId,
            @RequestParam(required = false) LocalDate doj,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return employeeService.getEmployees(searchStr, organizationId, designationId, doj, page, pageSize);
    }

    @PostMapping
    public EmployeeDTO createEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.createEmployee(employeeRequestDTO);
    }
}

package com.employee.management.app.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.employee.management.app.Payload.EmployeeCommunicationRequestDTO;
import com.employee.management.app.Payload.EmployeeCommunicationResponseDTO;

public interface EmployeeCommunicationService {

	Page<EmployeeCommunicationResponseDTO> getEmployeeCommunications(String searchStr, Integer employeeId, Boolean isPermanent, 
            int page, int pageSize, Sort sort);

    EmployeeCommunicationResponseDTO createEmployeeCommunication(EmployeeCommunicationRequestDTO employeeCommunicationRequestDTO);

    EmployeeCommunicationResponseDTO updateEmployeeCommunication(Integer id, EmployeeCommunicationRequestDTO employeeCommunicationRequestDTO);

    void deleteEmployeeCommunication(Integer id);
}

package com.employee.management.app.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.employee.management.app.Entities.Employee;
import com.employee.management.app.Entities.EmployeeCommunication;
import com.employee.management.app.Exception.ResourceNotFoundException;
import com.employee.management.app.Exception.UniqueConstraintViolationException;
import com.employee.management.app.Payload.EmployeeCommunicationRequestDTO;
import com.employee.management.app.Payload.EmployeeCommunicationResponseDTO;
import com.employee.management.app.Repository.EmployeeCommunicationRepository;
import com.employee.management.app.Repository.EmployeeRepository;
import com.employee.management.app.Service.EmployeeCommunicationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.modelmapper.ModelMapper;

@Service
@Transactional
public class EmployeeCommunicationServiceImpl implements EmployeeCommunicationService {

    @Autowired
    private EmployeeCommunicationRepository employeeCommunicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper; // Mapper bean

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<EmployeeCommunicationResponseDTO> getEmployeeCommunications(String searchStr, Integer employeeId, Boolean isPermanent, 
                                                                            int page, int pageSize, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeCommunication> cq = cb.createQuery(EmployeeCommunication.class);
        Root<EmployeeCommunication> root = cq.from(EmployeeCommunication.class);

        Predicate predicate = cb.conjunction();
        if (searchStr != null && !searchStr.isEmpty()) {
            predicate = cb.and(predicate, cb.or(
                cb.like(root.get("address"), "%" + searchStr + "%"),
                cb.like(root.get("city"), "%" + searchStr + "%"),
                cb.like(root.get("country"), "%" + searchStr + "%"),
                cb.like(root.get("state"), "%" + searchStr + "%")
            ));
        }
        if (employeeId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("employeeId"), employeeId));
        }
        if (isPermanent != null) {
            predicate = cb.and(predicate, cb.equal(root.get("isPermanent"), isPermanent));
        }

        cq.where(predicate);

        TypedQuery<EmployeeCommunication> query = entityManager.createQuery(cq);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<EmployeeCommunication> employeeCommunications = query.getResultList();

        long totalRecords = countEmployeeCommunications(predicate);

        Page<EmployeeCommunication> employeeCommunicationPage = new PageImpl<>(employeeCommunications, pageRequest, totalRecords);
        return employeeCommunicationPage.map(this::mapToResponseDTO);
    }

    private long countEmployeeCommunications(Predicate predicate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<EmployeeCommunication> root = countQuery.from(EmployeeCommunication.class);
        countQuery.select(cb.count(root)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public EmployeeCommunicationResponseDTO createEmployeeCommunication(EmployeeCommunicationRequestDTO dto) {
        EmployeeCommunication communication = convertToEntity(dto);
        try {
            EmployeeCommunication savedCommunication = employeeCommunicationRepository.save(communication);
            return mapToResponseDTO(savedCommunication);
        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
            throw new UniqueConstraintViolationException("Communication with the same details already exists.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while saving the communication details.");
        }
    }

    @Override
    public EmployeeCommunicationResponseDTO updateEmployeeCommunication(Integer id, EmployeeCommunicationRequestDTO dto) {
        EmployeeCommunication communication = employeeCommunicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee communication not found with id: " + id));
        
        modelMapper.map(dto, communication); // Using ModelMapper to map DTO to entity
        
        EmployeeCommunication updatedCommunication = employeeCommunicationRepository.save(communication);
        return mapToResponseDTO(updatedCommunication);
    }

    @Override
    public void deleteEmployeeCommunication(Integer id) {
        if (!employeeCommunicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee communication not found with id: " + id);
        }
        employeeCommunicationRepository.deleteById(id);
    }

    private EmployeeCommunicationResponseDTO mapToResponseDTO(EmployeeCommunication communication) {
        // Using ModelMapper to map entity to DTO
        return modelMapper.map(communication, EmployeeCommunicationResponseDTO.class);
    }

    private EmployeeCommunication convertToEntity(EmployeeCommunicationRequestDTO dto) {
        EmployeeCommunication communication = modelMapper.map(dto, EmployeeCommunication.class);

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        communication.setEmployee(employee);

        return communication;
    }
}

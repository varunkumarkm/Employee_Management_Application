package com.employee.management.app.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Designation;
import com.employee.management.app.Entities.Employee;
import com.employee.management.app.Entities.Organization;
import com.employee.management.app.Exception.UniqueConstraintViolationException;
import com.employee.management.app.Payload.EmployeeRequestDTO;
import com.employee.management.app.Payload.EmployeeResponseDTO;
import com.employee.management.app.Repository.DesignationRepository;
import com.employee.management.app.Repository.EmployeeRepository;
import com.employee.management.app.Repository.OrganizationRepository;
import com.employee.management.app.Service.EmployeeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DesignationRepository designationRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;                    

    @PersistenceContext
    private EntityManager entityManager;

    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployees(String searchStr, Integer organizationId, Integer designationId, String doj, int page, int pageSize, Sort sort) {
    	
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        Join<Employee, Organization> organizationJoin = root.join("organization", JoinType.LEFT);
        Join<Employee, Designation> designationJoin = root.join("designation", JoinType.LEFT);

        Predicate predicate = cb.conjunction();
        
        if (searchStr != null && !searchStr.isEmpty()) {
            predicate = cb.and(predicate, cb.or(
                cb.like(root.get("firstName"), "%" + searchStr + "%"),
                cb.like(root.get("lastName"), "%" + searchStr + "%"),
                cb.like(root.get("empCode"), "%" + searchStr + "%")
            ));
        }
        if (organizationId != null) {
            predicate = cb.and(predicate, cb.equal(organizationJoin.get("id"), organizationId));
        }
        if (designationId != null) {
            predicate = cb.and(predicate, cb.equal(designationJoin.get("id"), designationId));
        }
        if (doj != null && !doj.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("doj"), LocalDate.parse(doj)));
        }

        cq.where(predicate);

        if (sort != null && sort.isSorted()) {
            List<Order> orderList = new ArrayList<>();
            sort.forEach(order -> {
                if (order.isAscending()) {
                    orderList.add(cb.asc(root.get(order.getProperty())));
                } else {
                    orderList.add(cb.desc(root.get(order.getProperty())));
                }
            });
            cq.orderBy(orderList);
        }

        TypedQuery<Employee> query = entityManager.createQuery(cq);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Employee> employees = query.getResultList();
        long totalRecords = countEmployees(predicate);

        Page<Employee> employeePage = new PageImpl<>(employees, pageRequest, totalRecords);
        return employeePage.map(this::mapToResponseDto);
    }


    private long countEmployees(Predicate predicate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Employee> root = countQuery.from(Employee.class);
        countQuery.select(cb.count(root)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private EmployeeResponseDTO mapToResponseDto(Employee employee) {
        return modelMapper.map(employee, EmployeeResponseDTO.class);
    }

    @Override
    public EmployeeRequestDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
    	
    	if (employeeRepository.existsByEmpCode(employeeRequestDTO.getEmpCode())) {
            throw new UniqueConstraintViolationException("Employee code already exists.");
        }
        if (employeeRepository.existsByEmail(employeeRequestDTO.getEmail())) {
            throw new UniqueConstraintViolationException("Email already exists.");
        }
        if (employeeRepository.existsByPhone(employeeRequestDTO.getPhone())) {
            throw new UniqueConstraintViolationException("Phone number already exists.");
        }
    	
    	
        Employee employee = convertToEntity(employeeRequestDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    private EmployeeRequestDTO convertToDTO(Employee employee) {
    	EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setId (employee.getId());
        dto.setEmpCode(employee.getEmpCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDoj(employee.getDoj());
 
        if (employee.getOrganization() != null) {
            dto.setOrganizationId(employee.getOrganization().getId());
        }
 
        if (employee.getDesignation() != null) {
            dto.setDesignationId(employee.getDesignation().getId());
        }
 
        return dto;
    }
 
	private Employee convertToEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setEmpCode(dto.getEmpCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDoj(dto.getDoj());
 
        Organization organization = organizationRepository.findById(dto.getOrganizationId()).orElse(null);
        Designation designation = designationRepository.findById(dto.getDesignationId()).orElse(null);
 
        employee.setOrganization(organization);
        employee.setDesignation(designation);
 
        return employee;
    }
}

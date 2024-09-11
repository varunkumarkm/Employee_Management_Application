package com.employee.management.app.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Department;
import com.employee.management.app.Entities.Organization;
import com.employee.management.app.Payload.DepartmentRequestDTO;
import com.employee.management.app.Payload.DepartmentResponseDTO;
import com.employee.management.app.Repository.DepartmentRepository;
import com.employee.management.app.Service.DepartmentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<DepartmentResponseDTO> getDepartments(String searchStr, Integer organizationId, int page, int pageSize, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> cq = cb.createQuery(Department.class);
        Root<Department> root = cq.from(Department.class);
        root.fetch("organization", JoinType.LEFT);

        Predicate predicate = cb.conjunction();
        if (searchStr != null && !searchStr.isEmpty()) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + searchStr + "%"));
        }
        if (organizationId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("organizationId"), organizationId));
        }

        cq.where(predicate);
        cq.orderBy(sort.stream()
                        .map(order -> order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty())))
                        .toArray(Order[]::new));

        TypedQuery<Department> query = entityManager.createQuery(cq);
        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        query.setMaxResults(pageRequest.getPageSize());

        List<Department> departments = query.getResultList();
        long totalRecords = countDepartments(predicate); 

        // Convert to DTO
        List<DepartmentResponseDTO> departmentDtos = departments.stream().map(department -> {
            // Debugging: Check if organization is fetched correctly
            Organization organization = department.getOrganization();
            if (organization != null) {
                System.out.println("Organization Name: " + organization.getName());
            } else {
                System.out.println("No Organization found for the Department");
            }

            DepartmentResponseDTO dto = new DepartmentResponseDTO();
            dto.setId(department.getId());
            dto.setName(department.getName());
            dto.setOrganizationId(department.getOrganization().getId());
            if (department.getOrganization() != null) {
                dto.setOrganizationName(department.getOrganization().getName());
            } else {
                dto.setOrganizationName(null);
            }
            return dto;
        }).toList();

        return new PageImpl<>(departmentDtos, pageRequest, totalRecords);
    }

    private long countDepartments(Predicate predicate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Department> root = countQuery.from(Department.class);
        countQuery.select(cb.count(root)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest) {
        Department department = new Department();
        department.setName(departmentRequest.getName());
        department.setId(departmentRequest.getOrganizationId());

        // Set date fields
        LocalDateTime now = LocalDateTime.now();
        department.setCreatedDate(now);
        department.setModifiedDate(now);
        department.setDeletedDate(null); // Set to null if not applicable
        Organization organization = departmentRepository.findOrganizationById(department.getId());

        department.setOrganization(organization);
        // Save the department
        department = departmentRepository.save(department);

        // Retrieve organization information if necessary
        

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setOrganizationId(department.getId());
        dto.setOrganizationName(organization != null ? organization.getName() : null);

        return dto;
    }
}
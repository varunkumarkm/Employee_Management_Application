package com.employee.management.app.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Designation;
import com.employee.management.app.Payload.DesignationResponseDTO;
import com.employee.management.app.Repository.DesignationRepository;
import com.employee.management.app.Service.DesignationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DesignationResponseDTO createDesignation(DesignationResponseDTO designationDTO) {
        try {
            Designation designation = new Designation();
            designation.setDesignationName(designationDTO.getDesignationName());
            designation.setCreatedDate(LocalDateTime.now());
            designation = designationRepository.save(designation);
            return mapToDTO(designation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating designation: " + e.getMessage());
        }
    }

    @Override
    public Page<DesignationResponseDTO> getDesignations(String searchStr, int page, int pageSize, Sort sort) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Designation> cq = cb.createQuery(Designation.class);
            Root<Designation> root = cq.from(Designation.class);

            Predicate predicate = cb.conjunction();
            if (searchStr != null && !searchStr.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("name"), "%" + searchStr + "%"));
            }

            cq.where(predicate);

            if (sort != null && !sort.isEmpty()) {
                List<Order> orders = sort.stream()
                    .map(order -> order.isAscending() 
                        ? cb.asc(root.get(order.getProperty())) 
                        : cb.desc(root.get(order.getProperty())))
                    .toList();
                cq.orderBy(orders);
            }

            TypedQuery<Designation> query = entityManager.createQuery(cq);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Designation> designations = query.getResultList();
            long totalRecords = countDesignations(predicate);  
            Page<Designation> designationPage = new PageImpl<>(designations, PageRequest.of(page - 1, pageSize, sort), totalRecords);

            return designationPage.map(this::mapToDTO);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching designations: " + e.getMessage());
        }
    }

    private long countDesignations(Predicate predicate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Designation> root = countQuery.from(Designation.class);
        countQuery.select(cb.count(root)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private DesignationResponseDTO mapToDTO(Designation designation) {
        DesignationResponseDTO dto = new DesignationResponseDTO();
        dto.setId(designation.getId());
        dto.setDesignationName(designation.getDesignationName());
        return dto;
    }
}

package com.employee.management.app.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.employee.management.app.Entities.Organization;
import com.employee.management.app.Exception.UniqueConstraintViolationException;
import com.employee.management.app.Payload.OrganizationRequestDTO;
import com.employee.management.app.Payload.OrganizationResponseDTO;
import com.employee.management.app.Repository.OrganizationRepository;
import com.employee.management.app.Service.OrganizationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrganizationRequestDTO createOrganization(OrganizationRequestDTO organizationDto) {
        if (organizationRepository.existsByName(organizationDto.getName())) {
            throw new UniqueConstraintViolationException("Organization name must be unique");
        }
        if (organizationRepository.existsByShortCode(organizationDto.getShortCode())) {
            throw new UniqueConstraintViolationException("Short code must be unique");
        }

        Organization organization = mapToEntity(organizationDto);
        organization.setCreatedDate(LocalDateTime.now());
        organization.setModifiedDate(LocalDateTime.now());
        organization.setDeletedDate(null);

        Organization savedOrganization = organizationRepository.save(organization);
        return mapToDto(savedOrganization);
    }

    @Override
    public Page<OrganizationResponseDTO> getOrganizations(String searchStr, int page, int pageSize, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Organization> cq = cb.createQuery(Organization.class);
        Root<Organization> root = cq.from(Organization.class);

        // Add filtering based on search string
        Predicate predicate = cb.conjunction();
        if (searchStr != null && !searchStr.isEmpty()) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + searchStr + "%"));
        }
        cq.where(predicate);

        // Add sorting logic
        if (sort != null && sort.isSorted()) {
            List<Order> orders = new ArrayList<>();
            sort.forEach(order -> {
                if (order.isAscending()) {
                    orders.add(cb.asc(root.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(root.get(order.getProperty())));
                }
            });
            cq.orderBy(orders);
        }

        // Execute the query with pagination
        TypedQuery<Organization> query = entityManager.createQuery(cq);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        // Get the results
        List<Organization> organizations = query.getResultList();
        long totalRecords = countOrganizations(predicate); // Custom method to count total records

        // Create a Page object for the result
        Page<Organization> organizationPage = new PageImpl<>(organizations, pageRequest, totalRecords);

        // Map entities to DTOs
        return organizationPage.map(organization -> {
            OrganizationResponseDTO dto = new OrganizationResponseDTO();
            dto.setId(organization.getId());
            dto.setName(organization.getName());
            dto.setCity(organization.getCity());
            dto.setCountry(organization.getCountry());
            dto.setState(organization.getState());
            dto.setPincode(organization.getPincode());
            return dto;
        });
    }


    // Method to count total records
    private long countOrganizations(Predicate predicate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Organization> root = countQuery.from(Organization.class);
        countQuery.select(cb.count(root)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Organization mapToEntity(OrganizationRequestDTO organizationDto) {
        return modelMapper.map(organizationDto, Organization.class);
    }

    private OrganizationRequestDTO mapToDto(Organization organization) {
        return modelMapper.map(organization, OrganizationRequestDTO.class);
    }
}

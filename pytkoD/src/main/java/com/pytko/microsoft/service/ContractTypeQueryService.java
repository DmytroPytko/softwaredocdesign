package com.pytko.microsoft.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.pytko.microsoft.domain.ContractType;
import com.pytko.microsoft.domain.*; // for static metamodels
import com.pytko.microsoft.repository.ContractTypeRepository;
import com.pytko.microsoft.service.dto.ContractTypeCriteria;

/**
 * Service for executing complex queries for {@link ContractType} entities in the database.
 * The main input is a {@link ContractTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContractType} or a {@link Page} of {@link ContractType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractTypeQueryService extends QueryService<ContractType> {

    private final Logger log = LoggerFactory.getLogger(ContractTypeQueryService.class);

    private final ContractTypeRepository contractTypeRepository;

    public ContractTypeQueryService(ContractTypeRepository contractTypeRepository) {
        this.contractTypeRepository = contractTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ContractType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContractType> findByCriteria(ContractTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContractType> specification = createSpecification(criteria);
        return contractTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContractType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractType> findByCriteria(ContractTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContractType> specification = createSpecification(criteria);
        return contractTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContractType> specification = createSpecification(criteria);
        return contractTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContractType> createSpecification(ContractTypeCriteria criteria) {
        Specification<ContractType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContractType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContractType_.name));
            }
            if (criteria.getResourcesId() != null) {
                specification = specification.and(buildSpecification(criteria.getResourcesId(),
                    root -> root.join(ContractType_.resources, JoinType.LEFT).get(Resource_.id)));
            }
        }
        return specification;
    }
}

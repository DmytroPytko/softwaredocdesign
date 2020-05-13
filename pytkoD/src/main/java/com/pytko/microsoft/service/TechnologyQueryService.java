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

import com.pytko.microsoft.domain.Technology;
import com.pytko.microsoft.domain.*; // for static metamodels
import com.pytko.microsoft.repository.TechnologyRepository;
import com.pytko.microsoft.service.dto.TechnologyCriteria;

/**
 * Service for executing complex queries for {@link Technology} entities in the database.
 * The main input is a {@link TechnologyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Technology} or a {@link Page} of {@link Technology} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TechnologyQueryService extends QueryService<Technology> {

    private final Logger log = LoggerFactory.getLogger(TechnologyQueryService.class);

    private final TechnologyRepository technologyRepository;

    public TechnologyQueryService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    /**
     * Return a {@link List} of {@link Technology} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Technology> findByCriteria(TechnologyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Technology> specification = createSpecification(criteria);
        return technologyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Technology} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Technology> findByCriteria(TechnologyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Technology> specification = createSpecification(criteria);
        return technologyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TechnologyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Technology> specification = createSpecification(criteria);
        return technologyRepository.count(specification);
    }

    /**
     * Function to convert {@link TechnologyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Technology> createSpecification(TechnologyCriteria criteria) {
        Specification<Technology> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Technology_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Technology_.name));
            }
            if (criteria.getEmployyesId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployyesId(),
                    root -> root.join(Technology_.employyes, JoinType.LEFT).get(Employee_.id)));
            }
            if (criteria.getResourcesId() != null) {
                specification = specification.and(buildSpecification(criteria.getResourcesId(),
                    root -> root.join(Technology_.resources, JoinType.LEFT).get(Resource_.id)));
            }
        }
        return specification;
    }
}

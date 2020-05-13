package com.pytko.microsoft.service.impl;

import com.pytko.microsoft.service.TechnologyService;
import com.pytko.microsoft.domain.Technology;
import com.pytko.microsoft.repository.TechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Technology}.
 */
@Service
@Transactional
public class TechnologyServiceImpl implements TechnologyService {

    private final Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);

    private final TechnologyRepository technologyRepository;

    public TechnologyServiceImpl(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    /**
     * Save a technology.
     *
     * @param technology the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Technology save(Technology technology) {
        log.debug("Request to save Technology : {}", technology);
        return technologyRepository.save(technology);
    }

    /**
     * Get all the technologies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Technology> findAll(Pageable pageable) {
        log.debug("Request to get all Technologies");
        return technologyRepository.findAll(pageable);
    }

    /**
     * Get all the technologies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Technology> findAllWithEagerRelationships(Pageable pageable) {
        return technologyRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one technology by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Technology> findOne(Long id) {
        log.debug("Request to get Technology : {}", id);
        return technologyRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the technology by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Technology : {}", id);
        technologyRepository.deleteById(id);
    }
}

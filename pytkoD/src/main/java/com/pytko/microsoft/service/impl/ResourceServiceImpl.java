package com.pytko.microsoft.service.impl;

import com.pytko.microsoft.service.ResourceService;
import com.pytko.microsoft.domain.Resource;
import com.pytko.microsoft.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Resource}.
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    /**
     * Save a resource.
     *
     * @param resource the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Resource save(Resource resource) {
        log.debug("Request to save Resource : {}", resource);
        return resourceRepository.save(resource);
    }

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Resource> findAll(Pageable pageable) {
        log.debug("Request to get all Resources");
        return resourceRepository.findAll(pageable);
    }

    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Resource> findOne(Long id) {
        log.debug("Request to get Resource : {}", id);
        return resourceRepository.findById(id);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resource : {}", id);
        resourceRepository.deleteById(id);
    }
}

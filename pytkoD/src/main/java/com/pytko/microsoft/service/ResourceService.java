package com.pytko.microsoft.service;

import com.pytko.microsoft.domain.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Resource}.
 */
public interface ResourceService {

    /**
     * Save a resource.
     *
     * @param resource the entity to save.
     * @return the persisted entity.
     */
    Resource save(Resource resource);

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Resource> findAll(Pageable pageable);

    /**
     * Get the "id" resource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Resource> findOne(Long id);

    /**
     * Delete the "id" resource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

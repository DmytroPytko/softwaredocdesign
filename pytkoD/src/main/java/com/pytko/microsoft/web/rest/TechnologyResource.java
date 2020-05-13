package com.pytko.microsoft.web.rest;

import com.pytko.microsoft.domain.Technology;
import com.pytko.microsoft.service.TechnologyService;
import com.pytko.microsoft.web.rest.errors.BadRequestAlertException;
import com.pytko.microsoft.service.dto.TechnologyCriteria;
import com.pytko.microsoft.service.TechnologyQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pytko.microsoft.domain.Technology}.
 */
@RestController
@RequestMapping("/api")
public class TechnologyResource {

    private final Logger log = LoggerFactory.getLogger(TechnologyResource.class);

    private static final String ENTITY_NAME = "technology";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnologyService technologyService;

    private final TechnologyQueryService technologyQueryService;

    public TechnologyResource(TechnologyService technologyService, TechnologyQueryService technologyQueryService) {
        this.technologyService = technologyService;
        this.technologyQueryService = technologyQueryService;
    }

    /**
     * {@code POST  /technologies} : Create a new technology.
     *
     * @param technology the technology to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technology, or with status {@code 400 (Bad Request)} if the technology has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/technologies")
    public ResponseEntity<Technology> createTechnology(@Valid @RequestBody Technology technology) throws URISyntaxException {
        log.debug("REST request to save Technology : {}", technology);
        if (technology.getId() != null) {
            throw new BadRequestAlertException("A new technology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Technology result = technologyService.save(technology);
        return ResponseEntity.created(new URI("/api/technologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /technologies} : Updates an existing technology.
     *
     * @param technology the technology to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technology,
     * or with status {@code 400 (Bad Request)} if the technology is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technology couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/technologies")
    public ResponseEntity<Technology> updateTechnology(@Valid @RequestBody Technology technology) throws URISyntaxException {
        log.debug("REST request to update Technology : {}", technology);
        if (technology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Technology result = technologyService.save(technology);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, technology.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /technologies} : get all the technologies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technologies in body.
     */
    @GetMapping("/technologies")
    public ResponseEntity<List<Technology>> getAllTechnologies(TechnologyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Technologies by criteria: {}", criteria);
        Page<Technology> page = technologyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /technologies/count} : count all the technologies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/technologies/count")
    public ResponseEntity<Long> countTechnologies(TechnologyCriteria criteria) {
        log.debug("REST request to count Technologies by criteria: {}", criteria);
        return ResponseEntity.ok().body(technologyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /technologies/:id} : get the "id" technology.
     *
     * @param id the id of the technology to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technology, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/technologies/{id}")
    public ResponseEntity<Technology> getTechnology(@PathVariable Long id) {
        log.debug("REST request to get Technology : {}", id);
        Optional<Technology> technology = technologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(technology);
    }

    /**
     * {@code DELETE  /technologies/:id} : delete the "id" technology.
     *
     * @param id the id of the technology to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/technologies/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        log.debug("REST request to delete Technology : {}", id);
        technologyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

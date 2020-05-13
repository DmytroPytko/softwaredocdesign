package com.pytko.microsoft.web.rest;

import com.pytko.microsoft.domain.ContractType;
import com.pytko.microsoft.service.ContractTypeService;
import com.pytko.microsoft.web.rest.errors.BadRequestAlertException;
import com.pytko.microsoft.service.dto.ContractTypeCriteria;
import com.pytko.microsoft.service.ContractTypeQueryService;

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
 * REST controller for managing {@link com.pytko.microsoft.domain.ContractType}.
 */
@RestController
@RequestMapping("/api")
public class ContractTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContractTypeResource.class);

    private static final String ENTITY_NAME = "contractType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractTypeService contractTypeService;

    private final ContractTypeQueryService contractTypeQueryService;

    public ContractTypeResource(ContractTypeService contractTypeService, ContractTypeQueryService contractTypeQueryService) {
        this.contractTypeService = contractTypeService;
        this.contractTypeQueryService = contractTypeQueryService;
    }

    /**
     * {@code POST  /contract-types} : Create a new contractType.
     *
     * @param contractType the contractType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractType, or with status {@code 400 (Bad Request)} if the contractType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contract-types")
    public ResponseEntity<ContractType> createContractType(@Valid @RequestBody ContractType contractType) throws URISyntaxException {
        log.debug("REST request to save ContractType : {}", contractType);
        if (contractType.getId() != null) {
            throw new BadRequestAlertException("A new contractType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractType result = contractTypeService.save(contractType);
        return ResponseEntity.created(new URI("/api/contract-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contract-types} : Updates an existing contractType.
     *
     * @param contractType the contractType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractType,
     * or with status {@code 400 (Bad Request)} if the contractType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contract-types")
    public ResponseEntity<ContractType> updateContractType(@Valid @RequestBody ContractType contractType) throws URISyntaxException {
        log.debug("REST request to update ContractType : {}", contractType);
        if (contractType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContractType result = contractTypeService.save(contractType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contract-types} : get all the contractTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractTypes in body.
     */
    @GetMapping("/contract-types")
    public ResponseEntity<List<ContractType>> getAllContractTypes(ContractTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContractTypes by criteria: {}", criteria);
        Page<ContractType> page = contractTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-types/count} : count all the contractTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contract-types/count")
    public ResponseEntity<Long> countContractTypes(ContractTypeCriteria criteria) {
        log.debug("REST request to count ContractTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contractTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contract-types/:id} : get the "id" contractType.
     *
     * @param id the id of the contractType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contract-types/{id}")
    public ResponseEntity<ContractType> getContractType(@PathVariable Long id) {
        log.debug("REST request to get ContractType : {}", id);
        Optional<ContractType> contractType = contractTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractType);
    }

    /**
     * {@code DELETE  /contract-types/:id} : delete the "id" contractType.
     *
     * @param id the id of the contractType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contract-types/{id}")
    public ResponseEntity<Void> deleteContractType(@PathVariable Long id) {
        log.debug("REST request to delete ContractType : {}", id);
        contractTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

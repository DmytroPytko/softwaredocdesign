package com.pytko.microsoft.service.impl;

import com.pytko.microsoft.service.ContractTypeService;
import com.pytko.microsoft.domain.ContractType;
import com.pytko.microsoft.repository.ContractTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ContractType}.
 */
@Service
@Transactional
public class ContractTypeServiceImpl implements ContractTypeService {

    private final Logger log = LoggerFactory.getLogger(ContractTypeServiceImpl.class);

    private final ContractTypeRepository contractTypeRepository;

    public ContractTypeServiceImpl(ContractTypeRepository contractTypeRepository) {
        this.contractTypeRepository = contractTypeRepository;
    }

    /**
     * Save a contractType.
     *
     * @param contractType the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ContractType save(ContractType contractType) {
        log.debug("Request to save ContractType : {}", contractType);
        return contractTypeRepository.save(contractType);
    }

    /**
     * Get all the contractTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContractType> findAll(Pageable pageable) {
        log.debug("Request to get all ContractTypes");
        return contractTypeRepository.findAll(pageable);
    }

    /**
     * Get one contractType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContractType> findOne(Long id) {
        log.debug("Request to get ContractType : {}", id);
        return contractTypeRepository.findById(id);
    }

    /**
     * Delete the contractType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContractType : {}", id);
        contractTypeRepository.deleteById(id);
    }
}

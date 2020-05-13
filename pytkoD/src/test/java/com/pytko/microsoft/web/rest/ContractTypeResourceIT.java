package com.pytko.microsoft.web.rest;

import com.pytko.microsoft.MicrosoftApp;
import com.pytko.microsoft.domain.ContractType;
import com.pytko.microsoft.domain.Resource;
import com.pytko.microsoft.repository.ContractTypeRepository;
import com.pytko.microsoft.service.ContractTypeService;
import com.pytko.microsoft.service.dto.ContractTypeCriteria;
import com.pytko.microsoft.service.ContractTypeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContractTypeResource} REST controller.
 */
@SpringBootTest(classes = MicrosoftApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ContractTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Autowired
    private ContractTypeService contractTypeService;

    @Autowired
    private ContractTypeQueryService contractTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractTypeMockMvc;

    private ContractType contractType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractType createEntity(EntityManager em) {
        ContractType contractType = new ContractType()
            .name(DEFAULT_NAME);
        return contractType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractType createUpdatedEntity(EntityManager em) {
        ContractType contractType = new ContractType()
            .name(UPDATED_NAME);
        return contractType;
    }

    @BeforeEach
    public void initTest() {
        contractType = createEntity(em);
    }

    @Test
    @Transactional
    public void createContractType() throws Exception {
        int databaseSizeBeforeCreate = contractTypeRepository.findAll().size();

        // Create the ContractType
        restContractTypeMockMvc.perform(post("/api/contract-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractType)))
            .andExpect(status().isCreated());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContractType testContractType = contractTypeList.get(contractTypeList.size() - 1);
        assertThat(testContractType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createContractTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractTypeRepository.findAll().size();

        // Create the ContractType with an existing ID
        contractType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractTypeMockMvc.perform(post("/api/contract-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractType)))
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractTypeRepository.findAll().size();
        // set the field null
        contractType.setName(null);

        // Create the ContractType, which fails.

        restContractTypeMockMvc.perform(post("/api/contract-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractType)))
            .andExpect(status().isBadRequest());

        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContractTypes() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList
        restContractTypeMockMvc.perform(get("/api/contract-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getContractType() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get the contractType
        restContractTypeMockMvc.perform(get("/api/contract-types/{id}", contractType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getContractTypesByIdFiltering() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        Long id = contractType.getId();

        defaultContractTypeShouldBeFound("id.equals=" + id);
        defaultContractTypeShouldNotBeFound("id.notEquals=" + id);

        defaultContractTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContractTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultContractTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContractTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContractTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList where name equals to DEFAULT_NAME
        defaultContractTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contractTypeList where name equals to UPDATED_NAME
        defaultContractTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContractTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList where name not equals to DEFAULT_NAME
        defaultContractTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the contractTypeList where name not equals to UPDATED_NAME
        defaultContractTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContractTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContractTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contractTypeList where name equals to UPDATED_NAME
        defaultContractTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContractTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList where name is not null
        defaultContractTypeShouldBeFound("name.specified=true");

        // Get all the contractTypeList where name is null
        defaultContractTypeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList where name contains DEFAULT_NAME
        defaultContractTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the contractTypeList where name contains UPDATED_NAME
        defaultContractTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContractTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList where name does not contain DEFAULT_NAME
        defaultContractTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the contractTypeList where name does not contain UPDATED_NAME
        defaultContractTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllContractTypesByResourcesIsEqualToSomething() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);
        Resource resources = ResourceResourceIT.createEntity(em);
        em.persist(resources);
        em.flush();
        contractType.addResources(resources);
        contractTypeRepository.saveAndFlush(contractType);
        Long resourcesId = resources.getId();

        // Get all the contractTypeList where resources equals to resourcesId
        defaultContractTypeShouldBeFound("resourcesId.equals=" + resourcesId);

        // Get all the contractTypeList where resources equals to resourcesId + 1
        defaultContractTypeShouldNotBeFound("resourcesId.equals=" + (resourcesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractTypeShouldBeFound(String filter) throws Exception {
        restContractTypeMockMvc.perform(get("/api/contract-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restContractTypeMockMvc.perform(get("/api/contract-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractTypeShouldNotBeFound(String filter) throws Exception {
        restContractTypeMockMvc.perform(get("/api/contract-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractTypeMockMvc.perform(get("/api/contract-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingContractType() throws Exception {
        // Get the contractType
        restContractTypeMockMvc.perform(get("/api/contract-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractType() throws Exception {
        // Initialize the database
        contractTypeService.save(contractType);

        int databaseSizeBeforeUpdate = contractTypeRepository.findAll().size();

        // Update the contractType
        ContractType updatedContractType = contractTypeRepository.findById(contractType.getId()).get();
        // Disconnect from session so that the updates on updatedContractType are not directly saved in db
        em.detach(updatedContractType);
        updatedContractType
            .name(UPDATED_NAME);

        restContractTypeMockMvc.perform(put("/api/contract-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContractType)))
            .andExpect(status().isOk());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeUpdate);
        ContractType testContractType = contractTypeList.get(contractTypeList.size() - 1);
        assertThat(testContractType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingContractType() throws Exception {
        int databaseSizeBeforeUpdate = contractTypeRepository.findAll().size();

        // Create the ContractType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractTypeMockMvc.perform(put("/api/contract-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractType)))
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContractType() throws Exception {
        // Initialize the database
        contractTypeService.save(contractType);

        int databaseSizeBeforeDelete = contractTypeRepository.findAll().size();

        // Delete the contractType
        restContractTypeMockMvc.perform(delete("/api/contract-types/{id}", contractType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

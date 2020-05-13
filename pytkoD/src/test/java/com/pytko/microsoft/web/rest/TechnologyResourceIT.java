package com.pytko.microsoft.web.rest;

import com.pytko.microsoft.MicrosoftApp;
import com.pytko.microsoft.domain.Technology;
import com.pytko.microsoft.domain.Employee;
import com.pytko.microsoft.domain.Resource;
import com.pytko.microsoft.repository.TechnologyRepository;
import com.pytko.microsoft.service.TechnologyService;
import com.pytko.microsoft.service.dto.TechnologyCriteria;
import com.pytko.microsoft.service.TechnologyQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TechnologyResource} REST controller.
 */
@SpringBootTest(classes = MicrosoftApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TechnologyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TechnologyRepository technologyRepository;

    @Mock
    private TechnologyRepository technologyRepositoryMock;

    @Mock
    private TechnologyService technologyServiceMock;

    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private TechnologyQueryService technologyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechnologyMockMvc;

    private Technology technology;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technology createEntity(EntityManager em) {
        Technology technology = new Technology()
            .name(DEFAULT_NAME);
        return technology;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technology createUpdatedEntity(EntityManager em) {
        Technology technology = new Technology()
            .name(UPDATED_NAME);
        return technology;
    }

    @BeforeEach
    public void initTest() {
        technology = createEntity(em);
    }

    @Test
    @Transactional
    public void createTechnology() throws Exception {
        int databaseSizeBeforeCreate = technologyRepository.findAll().size();

        // Create the Technology
        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isCreated());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeCreate + 1);
        Technology testTechnology = technologyList.get(technologyList.size() - 1);
        assertThat(testTechnology.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTechnologyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = technologyRepository.findAll().size();

        // Create the Technology with an existing ID
        technology.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = technologyRepository.findAll().size();
        // set the field null
        technology.setName(null);

        // Create the Technology, which fails.

        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTechnologies() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList
        restTechnologyMockMvc.perform(get("/api/technologies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTechnologiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(technologyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTechnologyMockMvc.perform(get("/api/technologies?eagerload=true"))
            .andExpect(status().isOk());

        verify(technologyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTechnologiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(technologyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTechnologyMockMvc.perform(get("/api/technologies?eagerload=true"))
            .andExpect(status().isOk());

        verify(technologyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTechnology() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get the technology
        restTechnologyMockMvc.perform(get("/api/technologies/{id}", technology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(technology.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getTechnologiesByIdFiltering() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        Long id = technology.getId();

        defaultTechnologyShouldBeFound("id.equals=" + id);
        defaultTechnologyShouldNotBeFound("id.notEquals=" + id);

        defaultTechnologyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTechnologyShouldNotBeFound("id.greaterThan=" + id);

        defaultTechnologyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTechnologyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTechnologiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList where name equals to DEFAULT_NAME
        defaultTechnologyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the technologyList where name equals to UPDATED_NAME
        defaultTechnologyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTechnologiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList where name not equals to DEFAULT_NAME
        defaultTechnologyShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the technologyList where name not equals to UPDATED_NAME
        defaultTechnologyShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTechnologiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTechnologyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the technologyList where name equals to UPDATED_NAME
        defaultTechnologyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTechnologiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList where name is not null
        defaultTechnologyShouldBeFound("name.specified=true");

        // Get all the technologyList where name is null
        defaultTechnologyShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTechnologiesByNameContainsSomething() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList where name contains DEFAULT_NAME
        defaultTechnologyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the technologyList where name contains UPDATED_NAME
        defaultTechnologyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTechnologiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList where name does not contain DEFAULT_NAME
        defaultTechnologyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the technologyList where name does not contain UPDATED_NAME
        defaultTechnologyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllTechnologiesByEmployyesIsEqualToSomething() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);
        Employee employyes = EmployeeResourceIT.createEntity(em);
        em.persist(employyes);
        em.flush();
        technology.addEmployyes(employyes);
        technologyRepository.saveAndFlush(technology);
        Long employyesId = employyes.getId();

        // Get all the technologyList where employyes equals to employyesId
        defaultTechnologyShouldBeFound("employyesId.equals=" + employyesId);

        // Get all the technologyList where employyes equals to employyesId + 1
        defaultTechnologyShouldNotBeFound("employyesId.equals=" + (employyesId + 1));
    }


    @Test
    @Transactional
    public void getAllTechnologiesByResourcesIsEqualToSomething() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);
        Resource resources = ResourceResourceIT.createEntity(em);
        em.persist(resources);
        em.flush();
        technology.addResources(resources);
        technologyRepository.saveAndFlush(technology);
        Long resourcesId = resources.getId();

        // Get all the technologyList where resources equals to resourcesId
        defaultTechnologyShouldBeFound("resourcesId.equals=" + resourcesId);

        // Get all the technologyList where resources equals to resourcesId + 1
        defaultTechnologyShouldNotBeFound("resourcesId.equals=" + (resourcesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTechnologyShouldBeFound(String filter) throws Exception {
        restTechnologyMockMvc.perform(get("/api/technologies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTechnologyMockMvc.perform(get("/api/technologies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTechnologyShouldNotBeFound(String filter) throws Exception {
        restTechnologyMockMvc.perform(get("/api/technologies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTechnologyMockMvc.perform(get("/api/technologies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTechnology() throws Exception {
        // Get the technology
        restTechnologyMockMvc.perform(get("/api/technologies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTechnology() throws Exception {
        // Initialize the database
        technologyService.save(technology);

        int databaseSizeBeforeUpdate = technologyRepository.findAll().size();

        // Update the technology
        Technology updatedTechnology = technologyRepository.findById(technology.getId()).get();
        // Disconnect from session so that the updates on updatedTechnology are not directly saved in db
        em.detach(updatedTechnology);
        updatedTechnology
            .name(UPDATED_NAME);

        restTechnologyMockMvc.perform(put("/api/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTechnology)))
            .andExpect(status().isOk());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeUpdate);
        Technology testTechnology = technologyList.get(technologyList.size() - 1);
        assertThat(testTechnology.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTechnology() throws Exception {
        int databaseSizeBeforeUpdate = technologyRepository.findAll().size();

        // Create the Technology

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnologyMockMvc.perform(put("/api/technologies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTechnology() throws Exception {
        // Initialize the database
        technologyService.save(technology);

        int databaseSizeBeforeDelete = technologyRepository.findAll().size();

        // Delete the technology
        restTechnologyMockMvc.perform(delete("/api/technologies/{id}", technology.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

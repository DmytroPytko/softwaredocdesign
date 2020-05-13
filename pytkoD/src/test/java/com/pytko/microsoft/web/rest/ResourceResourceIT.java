package com.pytko.microsoft.web.rest;

import com.pytko.microsoft.MicrosoftApp;
import com.pytko.microsoft.domain.Resource;
import com.pytko.microsoft.domain.Employee;
import com.pytko.microsoft.domain.ContractType;
import com.pytko.microsoft.domain.Project;
import com.pytko.microsoft.domain.Technology;
import com.pytko.microsoft.repository.ResourceRepository;
import com.pytko.microsoft.service.ResourceService;
import com.pytko.microsoft.service.dto.ResourceCriteria;
import com.pytko.microsoft.service.ResourceQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.pytko.microsoft.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@SpringBootTest(classes = MicrosoftApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ResourceResourceIT {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceQueryService resourceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceMockMvc;

    private Resource resource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .note(DEFAULT_NOTE);
        return resource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity(EntityManager em) {
        Resource resource = new Resource()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .note(UPDATED_NOTE);
        return resource;
    }

    @BeforeEach
    public void initTest() {
        resource = createEntity(em);
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testResource.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testResource.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource with an existing ID
        resource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }
    
    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }


    @Test
    @Transactional
    public void getResourcesByIdFiltering() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        Long id = resource.getId();

        defaultResourceShouldBeFound("id.equals=" + id);
        defaultResourceShouldNotBeFound("id.notEquals=" + id);

        defaultResourceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResourceShouldNotBeFound("id.greaterThan=" + id);

        defaultResourceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResourceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllResourcesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate equals to DEFAULT_START_DATE
        defaultResourceShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the resourceList where startDate equals to UPDATED_START_DATE
        defaultResourceShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate not equals to DEFAULT_START_DATE
        defaultResourceShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the resourceList where startDate not equals to UPDATED_START_DATE
        defaultResourceShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultResourceShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the resourceList where startDate equals to UPDATED_START_DATE
        defaultResourceShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate is not null
        defaultResourceShouldBeFound("startDate.specified=true");

        // Get all the resourceList where startDate is null
        defaultResourceShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultResourceShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the resourceList where startDate is greater than or equal to UPDATED_START_DATE
        defaultResourceShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate is less than or equal to DEFAULT_START_DATE
        defaultResourceShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the resourceList where startDate is less than or equal to SMALLER_START_DATE
        defaultResourceShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate is less than DEFAULT_START_DATE
        defaultResourceShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the resourceList where startDate is less than UPDATED_START_DATE
        defaultResourceShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where startDate is greater than DEFAULT_START_DATE
        defaultResourceShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the resourceList where startDate is greater than SMALLER_START_DATE
        defaultResourceShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllResourcesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate equals to DEFAULT_END_DATE
        defaultResourceShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the resourceList where endDate equals to UPDATED_END_DATE
        defaultResourceShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate not equals to DEFAULT_END_DATE
        defaultResourceShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the resourceList where endDate not equals to UPDATED_END_DATE
        defaultResourceShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultResourceShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the resourceList where endDate equals to UPDATED_END_DATE
        defaultResourceShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate is not null
        defaultResourceShouldBeFound("endDate.specified=true");

        // Get all the resourceList where endDate is null
        defaultResourceShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultResourceShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the resourceList where endDate is greater than or equal to UPDATED_END_DATE
        defaultResourceShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate is less than or equal to DEFAULT_END_DATE
        defaultResourceShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the resourceList where endDate is less than or equal to SMALLER_END_DATE
        defaultResourceShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate is less than DEFAULT_END_DATE
        defaultResourceShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the resourceList where endDate is less than UPDATED_END_DATE
        defaultResourceShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllResourcesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where endDate is greater than DEFAULT_END_DATE
        defaultResourceShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the resourceList where endDate is greater than SMALLER_END_DATE
        defaultResourceShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllResourcesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where note equals to DEFAULT_NOTE
        defaultResourceShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the resourceList where note equals to UPDATED_NOTE
        defaultResourceShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllResourcesByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where note not equals to DEFAULT_NOTE
        defaultResourceShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the resourceList where note not equals to UPDATED_NOTE
        defaultResourceShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllResourcesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultResourceShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the resourceList where note equals to UPDATED_NOTE
        defaultResourceShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllResourcesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where note is not null
        defaultResourceShouldBeFound("note.specified=true");

        // Get all the resourceList where note is null
        defaultResourceShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllResourcesByNoteContainsSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where note contains DEFAULT_NOTE
        defaultResourceShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the resourceList where note contains UPDATED_NOTE
        defaultResourceShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllResourcesByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where note does not contain DEFAULT_NOTE
        defaultResourceShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the resourceList where note does not contain UPDATED_NOTE
        defaultResourceShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllResourcesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        resource.setEmployee(employee);
        resourceRepository.saveAndFlush(resource);
        Long employeeId = employee.getId();

        // Get all the resourceList where employee equals to employeeId
        defaultResourceShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the resourceList where employee equals to employeeId + 1
        defaultResourceShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }


    @Test
    @Transactional
    public void getAllResourcesByContractTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        ContractType contractType = ContractTypeResourceIT.createEntity(em);
        em.persist(contractType);
        em.flush();
        resource.setContractType(contractType);
        resourceRepository.saveAndFlush(resource);
        Long contractTypeId = contractType.getId();

        // Get all the resourceList where contractType equals to contractTypeId
        defaultResourceShouldBeFound("contractTypeId.equals=" + contractTypeId);

        // Get all the resourceList where contractType equals to contractTypeId + 1
        defaultResourceShouldNotBeFound("contractTypeId.equals=" + (contractTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllResourcesByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        resource.setProject(project);
        resourceRepository.saveAndFlush(resource);
        Long projectId = project.getId();

        // Get all the resourceList where project equals to projectId
        defaultResourceShouldBeFound("projectId.equals=" + projectId);

        // Get all the resourceList where project equals to projectId + 1
        defaultResourceShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }


    @Test
    @Transactional
    public void getAllResourcesByTechnologiesIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        Technology technologies = TechnologyResourceIT.createEntity(em);
        em.persist(technologies);
        em.flush();
        resource.addTechnologies(technologies);
        resourceRepository.saveAndFlush(resource);
        Long technologiesId = technologies.getId();

        // Get all the resourceList where technologies equals to technologiesId
        defaultResourceShouldBeFound("technologiesId.equals=" + technologiesId);

        // Get all the resourceList where technologies equals to technologiesId + 1
        defaultResourceShouldNotBeFound("technologiesId.equals=" + (technologiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceShouldBeFound(String filter) throws Exception {
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restResourceMockMvc.perform(get("/api/resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResourceShouldNotBeFound(String filter) throws Exception {
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceMockMvc.perform(get("/api/resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceService.save(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).get();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .note(UPDATED_NOTE);

        restResourceMockMvc.perform(put("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedResource)))
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testResource.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testResource.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Create the Resource

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc.perform(put("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceService.save(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Delete the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.pytko.microsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties("resources")
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties("resources")
    private ContractType contractType;

    @ManyToOne
    @JsonIgnoreProperties("resources")
    private Project project;

    @ManyToMany(mappedBy = "resources")
    @JsonIgnore
    private Set<Technology> technologies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Resource startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Resource endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public Resource note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Resource employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public Resource contractType(ContractType contractType) {
        this.contractType = contractType;
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Project getProject() {
        return project;
    }

    public Resource project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public Resource technologies(Set<Technology> technologies) {
        this.technologies = technologies;
        return this;
    }

    public Resource addTechnologies(Technology technology) {
        this.technologies.add(technology);
        technology.getResources().add(this);
        return this;
    }

    public Resource removeTechnologies(Technology technology) {
        this.technologies.remove(technology);
        technology.getResources().remove(this);
        return this;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}

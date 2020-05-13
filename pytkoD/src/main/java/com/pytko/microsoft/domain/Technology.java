package com.pytko.microsoft.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Technology.
 */
@Entity
@Table(name = "technology")
public class Technology implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "technology_employyes",
               joinColumns = @JoinColumn(name = "technology_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "employyes_id", referencedColumnName = "id"))
    private Set<Employee> employyes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "technology_resources",
               joinColumns = @JoinColumn(name = "technology_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "resources_id", referencedColumnName = "id"))
    private Set<Resource> resources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Technology name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployyes() {
        return employyes;
    }

    public Technology employyes(Set<Employee> employees) {
        this.employyes = employees;
        return this;
    }

    public Technology addEmployyes(Employee employee) {
        this.employyes.add(employee);
        employee.getTechnologies().add(this);
        return this;
    }

    public Technology removeEmployyes(Employee employee) {
        this.employyes.remove(employee);
        employee.getTechnologies().remove(this);
        return this;
    }

    public void setEmployyes(Set<Employee> employees) {
        this.employyes = employees;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Technology resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Technology addResources(Resource resource) {
        this.resources.add(resource);
        resource.getTechnologies().add(this);
        return this;
    }

    public Technology removeResources(Resource resource) {
        this.resources.remove(resource);
        resource.getTechnologies().remove(this);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Technology)) {
            return false;
        }
        return id != null && id.equals(((Technology) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Technology{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

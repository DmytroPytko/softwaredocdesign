package com.pytko.microsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(max = 100)
    @Column(name = "surname", length = 100, nullable = false)
    private String surname;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "phone", unique = true)
    private String phone;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @OneToMany(mappedBy = "employee")
    private Set<Resource> resources = new HashSet<>();

    @ManyToMany(mappedBy = "employyes")
    @JsonIgnore
    private Set<Technology> technologies = new HashSet<>();

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

    public Employee name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Employee surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Employee phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Employee address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Employee resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Employee addResources(Resource resource) {
        this.resources.add(resource);
        resource.setEmployee(this);
        return this;
    }

    public Employee removeResources(Resource resource) {
        this.resources.remove(resource);
        resource.setEmployee(null);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public Employee technologies(Set<Technology> technologies) {
        this.technologies = technologies;
        return this;
    }

    public Employee addTechnologies(Technology technology) {
        this.technologies.add(technology);
        technology.getEmployyes().add(this);
        return this;
    }

    public Employee removeTechnologies(Technology technology) {
        this.technologies.remove(technology);
        technology.getEmployyes().remove(this);
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
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}

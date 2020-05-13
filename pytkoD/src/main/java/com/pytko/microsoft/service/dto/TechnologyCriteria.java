package com.pytko.microsoft.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pytko.microsoft.domain.Technology} entity. This class is used
 * in {@link com.pytko.microsoft.web.rest.TechnologyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /technologies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TechnologyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter employyesId;

    private LongFilter resourcesId;

    public TechnologyCriteria() {
    }

    public TechnologyCriteria(TechnologyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.employyesId = other.employyesId == null ? null : other.employyesId.copy();
        this.resourcesId = other.resourcesId == null ? null : other.resourcesId.copy();
    }

    @Override
    public TechnologyCriteria copy() {
        return new TechnologyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getEmployyesId() {
        return employyesId;
    }

    public void setEmployyesId(LongFilter employyesId) {
        this.employyesId = employyesId;
    }

    public LongFilter getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(LongFilter resourcesId) {
        this.resourcesId = resourcesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TechnologyCriteria that = (TechnologyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(employyesId, that.employyesId) &&
            Objects.equals(resourcesId, that.resourcesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        employyesId,
        resourcesId
        );
    }

    @Override
    public String toString() {
        return "TechnologyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (employyesId != null ? "employyesId=" + employyesId + ", " : "") +
                (resourcesId != null ? "resourcesId=" + resourcesId + ", " : "") +
            "}";
    }

}

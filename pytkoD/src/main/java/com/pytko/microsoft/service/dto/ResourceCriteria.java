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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.pytko.microsoft.domain.Resource} entity. This class is used
 * in {@link com.pytko.microsoft.web.rest.ResourceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resources?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ResourceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private StringFilter note;

    private LongFilter employeeId;

    private LongFilter contractTypeId;

    private LongFilter projectId;

    private LongFilter technologiesId;

    public ResourceCriteria() {
    }

    public ResourceCriteria(ResourceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.contractTypeId = other.contractTypeId == null ? null : other.contractTypeId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.technologiesId = other.technologiesId == null ? null : other.technologiesId.copy();
    }

    @Override
    public ResourceCriteria copy() {
        return new ResourceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(LongFilter contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getTechnologiesId() {
        return technologiesId;
    }

    public void setTechnologiesId(LongFilter technologiesId) {
        this.technologiesId = technologiesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResourceCriteria that = (ResourceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(note, that.note) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(contractTypeId, that.contractTypeId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(technologiesId, that.technologiesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        startDate,
        endDate,
        note,
        employeeId,
        contractTypeId,
        projectId,
        technologiesId
        );
    }

    @Override
    public String toString() {
        return "ResourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
                (contractTypeId != null ? "contractTypeId=" + contractTypeId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
                (technologiesId != null ? "technologiesId=" + technologiesId + ", " : "") +
            "}";
    }

}

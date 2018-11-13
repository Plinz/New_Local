package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Grade entity. This class is used in GradeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /grades?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GradeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter grade;

    private LongFilter userId;

    private LongFilter productTypeId;

    public GradeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getGrade() {
        return grade;
    }

    public void setGrade(IntegerFilter grade) {
        this.grade = grade;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(LongFilter productTypeId) {
        this.productTypeId = productTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GradeCriteria that = (GradeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(grade, that.grade) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(productTypeId, that.productTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        grade,
        userId,
        productTypeId
        );
    }

    @Override
    public String toString() {
        return "GradeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (grade != null ? "grade=" + grade + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (productTypeId != null ? "productTypeId=" + productTypeId + ", " : "") +
            "}";
    }

}

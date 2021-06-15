package com.pp.reservo.domain.repositories.specification;

import com.pp.reservo.domain.entities.Appointment;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.pp.reservo.domain.common.Domain.DEFAULT_END_DURATION_FILTER;

public class AppointmentSpecification implements Specification<Appointment> {

    private final String byName;
    private final Integer fromDuration;
    private final Integer toDuration;

    public AppointmentSpecification(String byName, Integer fromDuration, Integer toDuration) {
        this.byName = byName;
        this.fromDuration = fromDuration;
        this.toDuration = toDuration;
    }

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        if (byName != null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + byName + "%"))
            );
        }

        if (fromDuration != null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("duration"), fromDuration))
            );
        }

        if (toDuration != null && toDuration != DEFAULT_END_DURATION_FILTER) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.lessThan(root.get("duration"), toDuration))
            );
        }

        return p;
    }
}

package com.pp.reservo.domain.repositories.specification;

import com.pp.reservo.domain.entities.Reservation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ReservationSpecification implements Specification<Reservation> {

    private final Date byDate;
    private final Integer client;
    private final Integer employee;
    private final Integer appointment;

    public ReservationSpecification(Date byDate, Integer client, Integer employee, Integer appointment) {
        this.byDate = byDate;
        this.client = client;
        this.employee = employee;
        this.appointment = appointment;
    }

    @Override
    public Predicate toPredicate(Root<Reservation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        if (byDate != null) {
            p.getExpressions().add(
                criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), convertToLocalDateTimeViaInstant(byDate)))
            );
        }

        if (client != null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("client"), client))
            );
        }

        if (employee != null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("employee"), employee))
            );
        }

        if (appointment != null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("appointment"), appointment))
            );
        }

        return p;
    }

    public Timestamp convertToLocalDateTimeViaInstant(Date dateToConvert) {
        String pattern = "EEE MMM dd HH:mm:ss z yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(dateToConvert.toString()));

        return Timestamp.valueOf(localDateTime);
    }
}

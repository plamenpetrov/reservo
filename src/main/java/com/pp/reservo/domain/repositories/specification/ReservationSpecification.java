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

    public ReservationSpecification(Date byDate) {
        this.byDate = byDate;
    }

    @Override
    public Predicate toPredicate(Root<Reservation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        if (byDate != null) {
            p.getExpressions().add(
                criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), convertToLocalDateTimeViaInstant(byDate)))
            );
        }

//        if (bookTitle != null) {
//            Subquery<Book> bookSubquery = query.subquery(Book.class);
//            Root<Book> subqueryRoot = bookSubquery.from(Book.class);
//            bookSubquery.select(subqueryRoot);
//
//            bookSubquery.where(
//                    criteriaBuilder.and(
//                            criteriaBuilder.equal(root, subqueryRoot.get("author")),
//                            criteriaBuilder.equal(subqueryRoot.get("title"), bookTitle)
//                    )
//            );
//
//            p.getExpressions().add(criteriaBuilder.exists(bookSubquery));
//        }

        return p;
    }

    public Timestamp convertToLocalDateTimeViaInstant(Date dateToConvert) {
        String pattern = "EEE MMM dd HH:mm:ss z yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(dateToConvert.toString()));

        return Timestamp.valueOf(localDateTime);
    }

}

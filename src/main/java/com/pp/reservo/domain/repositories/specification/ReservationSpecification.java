package com.pp.reservo.domain.repositories.specification;

import com.pp.reservo.domain.entities.Reservation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ReservationSpecification implements Specification<Reservation> {

    private final String byDate;

    public ReservationSpecification(String byDate) {
        this.byDate = byDate;
    }

    @Override
    public Predicate toPredicate(Root<Reservation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        if (byDate != null) {
            p.getExpressions().add(
                criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), byDate))
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
}

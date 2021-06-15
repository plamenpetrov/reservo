package com.pp.reservo.domain.repositories.specification;

import com.pp.reservo.domain.entities.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EmployeeSpecification implements Specification<Employee> {

    private final String byName;

    public EmployeeSpecification(String byName) {
        this.byName = byName;
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        if (byName != null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + byName + "%"))
            );
        }

        return p;
    }
}

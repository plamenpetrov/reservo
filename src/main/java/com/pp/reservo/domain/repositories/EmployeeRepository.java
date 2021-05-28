package com.pp.reservo.domain.repositories;

import com.pp.reservo.domain.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employe, Integer> {
}

package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.infrastructure.services.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

}

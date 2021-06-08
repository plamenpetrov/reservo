package com.pp.reservo.infrastructure.services;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(Integer employeeId) throws EntityNotFoundException;

    EmployeeDTO storeEmployee(EmployeeDTO employeeDTO);

    void deleteEmployee(Integer employeeId);
}

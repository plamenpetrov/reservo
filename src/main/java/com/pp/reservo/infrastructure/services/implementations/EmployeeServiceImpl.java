package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return this.employeeRepository
                .findAll()
                .stream()
                .map(a -> this.modelMapper.map(a, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer employeeId) throws EntityNotFoundException {
        return this.employeeRepository
                .findById(employeeId)
                .map(e -> this.modelMapper.map(e, EmployeeDTO.class))
                .orElseThrow(() ->
                        new EntityNotFoundException("Employee with the given id was not found!"));
    }

    @Override
    public EmployeeDTO storeEmployee(EmployeeDTO employeeDTO) {
        this.employeeRepository
                .saveAndFlush(this.modelMapper.map(employeeDTO, Employee.class));

        return employeeDTO;
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        if(employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        }
    }
}

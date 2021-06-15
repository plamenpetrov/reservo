package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeUpdatedDataEventDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.events.publishers.EmployeeEventPublisher;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.domain.repositories.specification.EmployeeSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pp.reservo.domain.common.Domain.PAGE_SIZE;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeEventPublisher employeeEventPublisher;

    public EmployeeServiceImpl(ModelMapper modelMapper,
                               EmployeeRepository employeeRepository,
                               EmployeeEventPublisher employeeEventPublisher
    ) {
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.employeeEventPublisher = employeeEventPublisher;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees(String byName, Integer page, String sortBy) {
        return this.employeeRepository
                .findAll(new EmployeeSpecification(
                        byName
                    ),

                    PageRequest.of(
                        page,
                        PAGE_SIZE,
                        Sort.Direction.ASC,
                        sortBy
                ))
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

        publishEventEmployeeStored(employeeDTO);

        return employeeDTO;
    }

    private void publishEventEmployeeStored(EmployeeDTO employeeDTO) {
        if(employeeDTO.getId() == null) {
            EmployeeCreatedDataEventDTO employeeCreatedDataEventDTO = this.modelMapper.map(employeeDTO, EmployeeCreatedDataEventDTO.class);
            employeeEventPublisher.publishEmployeeStored(employeeCreatedDataEventDTO);
        } else {
            EmployeeUpdatedDataEventDTO employeeUpdatedDataEventDTO = this.modelMapper.map(employeeDTO, EmployeeUpdatedDataEventDTO.class);
            employeeEventPublisher.publishEmployeeStored(employeeUpdatedDataEventDTO);
        }
    }

    private void publishEventEmployeeDelete(EmployeeDTO employeeDTO) {
        BaseDataEventDTO employeeDataEventDTO = this.modelMapper.map(employeeDTO, EmployeeDeletedDataEventDTO.class);
        employeeEventPublisher.publishEmployeeDeleted(employeeDataEventDTO);
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        if(employeeRepository.existsById(employeeId)) {
            EmployeeDTO employeeDTO = getEmployeeById(employeeId);
            employeeRepository.deleteById(employeeId);
            publishEventEmployeeDelete(employeeDTO);
        }
    }
}

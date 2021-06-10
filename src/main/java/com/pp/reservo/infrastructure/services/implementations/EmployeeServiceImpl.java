package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeUpdatedDataEventDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.ports.kafka.builders.BaseEventMessageBuilder;
import com.pp.reservo.infrastructure.ports.kafka.publisher.EmployeePublisher;
import com.pp.reservo.infrastructure.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeePublisher employeePublisher;
    private final BaseEventMessageBuilder eventMessageBuilder;

    public EmployeeServiceImpl(ModelMapper modelMapper, EmployeeRepository employeeRepository, EmployeePublisher employeePublisher, BaseEventMessageBuilder eventMessageBuilder) {
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.employeePublisher = employeePublisher;
        this.eventMessageBuilder = eventMessageBuilder;
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

        publishEventEmployeeChange(employeeDTO);

        return employeeDTO;
    }

    private void publishEvent(BaseDataEventDTO eventDataEventDTO) {
        employeePublisher.publishEvent(eventMessageBuilder.buildMessage(eventDataEventDTO));
    }

    private void publishEventEmployeeChange(EmployeeDTO employeeDTO) {
        BaseDataEventDTO employeeDataEventDTO = mapDataDTO(employeeDTO);
        publishEvent(employeeDataEventDTO);
    }

    private BaseDataEventDTO mapDataDTO(EmployeeDTO employeeDTO) {
        if(employeeDTO.getId() == null) {
            return this.modelMapper.map(employeeDTO, EmployeeCreatedDataEventDTO.class);
        } else {
            return this.modelMapper.map(employeeDTO, EmployeeUpdatedDataEventDTO.class);
        }
    }

    private void publishEventEmployeeDelete(EmployeeDTO employeeDTO) {
        BaseDataEventDTO employeeDataEventDTO = this.modelMapper.map(employeeDTO, EmployeeDeletedDataEventDTO.class);
        publishEvent(employeeDataEventDTO);
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

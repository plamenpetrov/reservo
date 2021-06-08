package com.pp.reservo.infrastructure.ports.kafka.listener;


import com.pp.reservo.domain.commands.employee.CreateEmployeeCommand;
import com.pp.reservo.domain.commands.employee.DeleteEmployeeCommand;
import com.pp.reservo.domain.commands.employee.UpdateEmployeeCommand;
import com.pp.reservo.domain.common.CommandExecutor;
import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.employee.EmployeeUpdatedDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.EmployeeEventSink;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableBinding(EmployeeEventSink.class)
public class EmployeeListener {

    private final ModelMapper modelMapper;

    @Autowired
    private final CommandExecutor commandExecutor;

    @Autowired
    private final CreateEmployeeCommand createEmployeeCommand;

    @Autowired
    private final UpdateEmployeeCommand updateEmployeeCommand;

    @Autowired
    private final DeleteEmployeeCommand deleteEmployeeCommand;

    @StreamListener(
            value = EmployeeEventSink.EMPLOYEE_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.createEmployee'"
    )
    public void onEmployeeCreated(BaseEvent<EmployeeCreatedDataEventDTO> event) {
        EmployeeDTO employeeDTO = this.modelMapper.map(event.getData(), EmployeeDTO.class);

        createEmployeeCommand.setEmployeeDTO(employeeDTO);

        commandExecutor.executeCommand(createEmployeeCommand);
    }

    @StreamListener(
            value = EmployeeEventSink.EMPLOYEE_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.updateEmployee'"
    )
    public void onEmployeeUpdated(BaseEvent<EmployeeUpdatedDataEventDTO> event) {
        EmployeeDTO employeeDTO = this.modelMapper.map(event.getData(), EmployeeDTO.class);

        updateEmployeeCommand.setEmployeeDTO(employeeDTO);

        commandExecutor.executeCommand(updateEmployeeCommand);
    }

    @StreamListener(
            value = EmployeeEventSink.EMPLOYEE_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.deleteEmployee'"
    )
    public void onEmployeeDeleted(BaseEvent<EmployeeDeletedDataEventDTO> event) {
        deleteEmployeeCommand.setEmployeeId(event.getData().getEmployeeId());

        commandExecutor.executeCommand(deleteEmployeeCommand);
    }
}

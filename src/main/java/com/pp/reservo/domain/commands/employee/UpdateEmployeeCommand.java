package com.pp.reservo.domain.commands.employee;

import com.pp.reservo.domain.common.Command;
import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.infrastructure.services.EmployeeService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Setter
@Service("updateEmployeeCommand")
public class UpdateEmployeeCommand implements Command {

    private final EmployeeService employeeService;
    private EmployeeDTO employeeDTO;

    public UpdateEmployeeCommand(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void execute() {
        this.employeeService.storeEmployee(employeeDTO);
    }
}

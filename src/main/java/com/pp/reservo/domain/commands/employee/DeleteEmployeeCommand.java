package com.pp.reservo.domain.commands.employee;

import com.pp.reservo.domain.common.Command;
import com.pp.reservo.infrastructure.services.EmployeeService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Setter
@Service("deleteEmployeeCommand")
public class DeleteEmployeeCommand implements Command {
    
    private final EmployeeService employeeService;
    private Integer employeeId;

    public DeleteEmployeeCommand(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void execute() {
        this.employeeService.deleteEmployee(employeeId);
    }
}

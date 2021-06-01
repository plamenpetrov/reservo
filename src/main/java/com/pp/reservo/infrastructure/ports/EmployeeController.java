package com.pp.reservo.infrastructure.ports;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.models.requests.CreateEmployeeRequest;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api/employees",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class EmployeeController {

    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;

    public EmployeeController(ModelMapper modelMapper, EmployeeService employeeService) {
        this.modelMapper = modelMapper;
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @RequestMapping(
            value = "/{employeeId}",
            method = RequestMethod.GET
    )
    public EmployeeDTO getEmployee(@PathVariable(value = "employeeId", required = false) Integer employeeId) throws EntityNotFoundException {
        return employeeService.getEmployeeById(employeeId);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody CreateEmployeeRequest employee) {
        this.employeeService
                .addEmployee(this.modelMapper.map(employee, EmployeeDTO.class));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
    }
}

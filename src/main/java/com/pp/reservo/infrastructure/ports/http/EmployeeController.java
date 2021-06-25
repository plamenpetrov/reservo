package com.pp.reservo.infrastructure.ports.http;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.models.requests.StoreEmployeeRequest;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(
        value = "/api/employee",
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
    public List<EmployeeDTO> getEmployees(
            @RequestParam(required = false) String byName,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy

    ) {
        return employeeService.getAllEmployees(byName, page.orElse(0), sortBy.orElse("id"));
    }

    @RequestMapping(
            value = "/{employeeId}",
            method = RequestMethod.GET
    )
    public EmployeeDTO getEmployee(@PathVariable(value = "employeeId", required = false) Integer employeeId) throws EntityNotFoundException {
        return employeeService.getEmployeeById(employeeId);
    }

    @PostMapping
    public ResponseEntity<Employee> storeEmployee(@Valid @RequestBody StoreEmployeeRequest employee) {
        this.employeeService
                .storeEmployee(this.modelMapper.map(employee, EmployeeDTO.class));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
    }
}

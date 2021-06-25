package com.pp.reservo.unit.repository;

import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.unit.factories.EmployeeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void findAllEmployees() {
        employeeRepository.save(EmployeeFactory.create());
        assertEquals(1, employeeRepository.findAll().size());
    }

    @Test
    public void findEmployeeById() {
        Employee employee = employeeRepository.save(EmployeeFactory.create());

        Employee employeeDB = employeeRepository.getById(employee.getId());

        assertNotNull(employeeDB);
    }

    @Test
    public void createEmployee() {
        Employee employee = EmployeeFactory.create();

        Employee employeeDB = employeeRepository.save(employee);

        assertEquals(employee.getName(), employeeDB.getName());
    }

    @Test
    public void deleteEmployee() {
        Employee employee = employeeRepository.save(EmployeeFactory.create());

        employeeRepository.deleteById(employee.getId());

        Optional<Employee> employeeDB = employeeRepository.findById(employee.getId());

        assertThat(employeeDB.isEmpty());
    }
}

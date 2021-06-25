package com.pp.reservo.unit.services;

import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.events.publishers.EmployeeEventPublisher;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.domain.repositories.specification.EmployeeSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.implementations.EmployeeServiceImpl;
import com.pp.reservo.unit.factories.EmployeeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.pp.reservo.domain.common.Domain.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeEventPublisher employeeEventPublisher;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private List<Employee> employeeEntities;

    @BeforeEach
    public void initService() {
        employeeService = new EmployeeServiceImpl(modelMapper, employeeRepository, employeeEventPublisher);
        employeeEntities = initEmployees();
    }

    private List<Employee> initEmployees() {
        List<Employee> employeeEntities = new ArrayList<>();
        employeeEntities.add(EmployeeFactory.create());
        employeeEntities.add(EmployeeFactory.create());

        return employeeEntities;
    }
    
    @ParameterizedTest
    @MethodSource("allEmployeesDataProvider")
    public void should_return_all_employees_without_filters(String byName, Integer page, String sortBy, Integer expected) {
        testGetEmployees(byName, page, sortBy, expected);
    }

    @Test
    public void should_return_employees_filtered_by_name() {
        Employee employee = employeeEntities.get(0);
        String byName = employee.getName();
        employeeEntities.remove(0);

        testGetEmployees(byName, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 1);
    }

    @Test
    public void should_not_return_employees_filtered_by_name() {
        employeeEntities.clear();
        testGetEmployees("testEmployee", DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 0);
    }

    private void testGetEmployees(String byName, Integer page, String sortBy, Integer expected) {
        int size = PAGE_SIZE;

        Page<Employee> paginatedEmployees = new PageImpl(employeeEntities);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);

        EmployeeSpecification employeeSpecification = new EmployeeSpecification(byName);

        EmployeeServiceImpl employeeServiceSpy = Mockito.spy(employeeService);

        doReturn(employeeSpecification)
                .when(employeeServiceSpy)
                .getSpecificationInstance(byName);

        when(employeeRepository.findAll(employeeSpecification, pageable))
                .thenReturn(paginatedEmployees);

        List<EmployeeDTO> employeesDB = employeeServiceSpy.getAllEmployees(byName, page, sortBy);

        assertEquals(expected, employeesDB.size());
    }

    public static Stream<Arguments> allEmployeesDataProvider() {
        return Stream.of(
                Arguments.of("", DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 2),
                Arguments.of("", DEFAULT_PAGE, "name", 2),
                Arguments.of("", 2, "name", 2)
        );
    }

    @Test
    public void should_find_existing_employee() throws EntityNotFoundException {
        Employee employee = EmployeeFactory.create();
        Integer employeeId = employee.getId();

        EmployeeServiceImpl employeeServiceSpy = Mockito.spy(employeeService);

        doReturn(Optional.of(employee))
                .when(employeeServiceSpy)
                .findById(employeeId);

        EmployeeDTO employeesDB = employeeServiceSpy.getEmployeeById(employeeId);

        assertEquals(employee.getName(), employeesDB.getName());
        assertEquals(employee.getId(), employeesDB.getId());
    }

    @Test
    public void should_store_new_employee() {
        Employee employee = new Employee();
        employee.setName(employee.getName());

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getName());

        EmployeeServiceImpl employeeServiceSpy = Mockito.spy(employeeService);

        doReturn(employee)
                .when(employeeRepository)
                .saveAndFlush(employee);

        EmployeeDTO storedEmployee = employeeServiceSpy.storeEmployee(employeeDTO);

        assertEquals(employeeDTO, storedEmployee);
    }

    @Test
    public void should_delete_existing_employee() {
        Employee employee = employeeEntities.get(0);
        Integer employeeId = employee.getId();

        when(employeeRepository.existsById(employeeId))
                .thenReturn(true);

        EmployeeDTO employeeDTO = this.modelMapper.map(employee, EmployeeDTO.class);

        EmployeeServiceImpl employeeServiceSpy = Mockito.spy(employeeService);

        doReturn(employeeDTO)
                .when(employeeServiceSpy)
                .getEmployeeById(employeeId);

        employeeServiceSpy.deleteEmployee(employeeId);

        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(employeeId);
    }
}

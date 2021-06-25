package com.pp.reservo.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.reservo.domain.dto.EmployeeDTO;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.infrastructure.ports.http.EmployeeController;
import com.pp.reservo.infrastructure.services.EmployeeService;
import com.pp.reservo.unit.factories.EmployeeFactory;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.pp.reservo.domain.common.Domain.DEFAULT_PAGE;
import static com.pp.reservo.domain.common.Domain.DEFAULT_SORT_COLUMN;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureJsonTesters
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    @Value("${reservo.auth.basic.username}")
    protected String basicAuthUsername;

    @Value("${reservo.auth.basic.password}")
    protected String basicAuthPassword;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_all_employee_without_filters() throws Exception {
        List<EmployeeDTO> employees = new ArrayList<>();

        Employee employee = EmployeeFactory.create();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());

        employees.add(employeeDTO);

        given(employeeService.getAllEmployees(null, DEFAULT_PAGE, DEFAULT_SORT_COLUMN))
                .willReturn(employees);

        this.mockMvc.perform(get("/api/employee")
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(employee.getId())))
                .andExpect(jsonPath("$[0].name", is(employee.getName())));
    }

    @Test
    public void should_return_error_if_no_authentication() throws Exception {
        this.mockMvc.perform(get("/api/employee"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_return_employee_by_id() throws Exception {
        Employee employee = EmployeeFactory.create();
        employee.setId(1);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());

        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(employeeDTO);

        this.mockMvc.perform(get("/api/employee/" + employee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(employee.getId())))
                .andExpect(jsonPath("$.name", is(employee.getName())));
    }

    @Test
    public void should_create_employee() throws Exception {
        Employee employee = EmployeeFactory.create();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(employee);

        given(employeeService.storeEmployee(employeeDTO))
                .willReturn(employeeDTO);

        this.mockMvc.perform(post("/api/employee/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_update_employee() throws Exception {
        Employee employee = EmployeeFactory.create();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(employee);

        given(employeeService.storeEmployee(employeeDTO))
                .willReturn(employeeDTO);

        this.mockMvc.perform(post("/api/employee/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_delete_employee_by_id() throws Exception {
        Employee employee = EmployeeFactory.create();
        employee.setId(1);

        doNothing()
                .when(employeeService)
                .deleteEmployee(employee.getId());

        this.mockMvc.perform(delete("/api/employee/" + employee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }
}

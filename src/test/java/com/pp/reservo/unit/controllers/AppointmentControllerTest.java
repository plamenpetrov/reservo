package com.pp.reservo.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.infrastructure.ports.http.AppointmentController;
import com.pp.reservo.infrastructure.services.AppointmentService;
import com.pp.reservo.unit.factories.AppointmentFactory;
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
@WebMvcTest(controllers = AppointmentController.class)
public class AppointmentControllerTest {

    @Value("${reservo.auth.basic.username}")
    protected String basicAuthUsername;

    @Value("${reservo.auth.basic.password}")
    protected String basicAuthPassword;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_all_appointment_without_filters() throws Exception {
        List<AppointmentDTO> appointments = new ArrayList<>();

        Appointment appointment = AppointmentFactory.create();
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setName(appointment.getName());
        appointmentDTO.setDuration(appointment.getDuration());

        appointments.add(appointmentDTO);

        given(appointmentService.getAllAppointments(null, 0, 0, DEFAULT_PAGE, DEFAULT_SORT_COLUMN))
                .willReturn(appointments);

        this.mockMvc.perform(get("/api/appointments")
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(appointment.getId())))
                .andExpect(jsonPath("$[0].name", is(appointment.getName())))
                .andExpect(jsonPath("$[0].duration", is(appointment.getDuration())));
    }

    @Test
    public void should_return_error_if_no_authentication() throws Exception {
        this.mockMvc.perform(get("/api/appointments"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_return_appointment_by_id() throws Exception {
        Appointment appointment = AppointmentFactory.create();
        appointment.setId(1);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setName(appointment.getName());

        given(appointmentService.getAppointmentById(appointment.getId()))
                .willReturn(appointmentDTO);

        this.mockMvc.perform(get("/api/appointments/" + appointment.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(appointment.getId())))
                .andExpect(jsonPath("$.name", is(appointment.getName())));
    }

    @Test
    public void should_create_appointment() throws Exception {
        Appointment appointment = AppointmentFactory.create();
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setName(appointment.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(appointment);

        given(appointmentService.storeAppointment(appointmentDTO))
                .willReturn(appointmentDTO);

        this.mockMvc.perform(post("/api/appointments/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_update_appointment() throws Exception {
        Appointment appointment = AppointmentFactory.create();
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setName(appointment.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(appointment);

        given(appointmentService.storeAppointment(appointmentDTO))
                .willReturn(appointmentDTO);

        this.mockMvc.perform(post("/api/appointments/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_delete_appointment_by_id() throws Exception {
        Appointment appointment = AppointmentFactory.create();
        appointment.setId(1);

        doNothing()
                .when(appointmentService)
                .deleteAppointment(appointment.getId());

        this.mockMvc.perform(delete("/api/appointments/" + appointment.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }
}

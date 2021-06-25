package com.pp.reservo.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.models.requests.StoreReservationRequest;
import com.pp.reservo.infrastructure.ports.http.ReservationController;
import com.pp.reservo.infrastructure.services.ReservationService;
import com.pp.reservo.unit.factories.ReservationFactory;
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
@WebMvcTest(controllers = ReservationController.class)
public class ReservationControllerTest {

    @Value("${reservo.auth.basic.username}")
    protected String basicAuthUsername;

    @Value("${reservo.auth.basic.password}")
    protected String basicAuthPassword;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_all_reservation_without_filters() throws Exception {
        List<ReservationDTO> reservations = new ArrayList<>();

        Reservation reservation = ReservationFactory.create();
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmployeeId(1);
        reservationDTO.setAppointmentId(1);
        reservationDTO.setClientId(1);
        reservationDTO.setDuration(reservation.getDuration());
        reservationDTO.setStartAt(reservation.getStartAt());

        reservations.add(reservationDTO);

        given(reservationService.getAllReservations(null, null, null, null, DEFAULT_PAGE, DEFAULT_SORT_COLUMN))
                .willReturn(reservations);

        this.mockMvc.perform(get("/api/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].appointmentId", is(1)))
                .andExpect(jsonPath("$[0].clientId", is(1)))
                .andExpect(jsonPath("$[0].employeeId", is(1)));
    }

    @Test
    public void should_return_error_if_no_authentication() throws Exception {
        this.mockMvc.perform(get("/api/reservation"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_return_reservation_by_id() throws Exception {
        Reservation reservation = ReservationFactory.create();
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmployeeId(1);
        reservationDTO.setAppointmentId(1);
        reservationDTO.setClientId(1);
        reservationDTO.setDuration(reservation.getDuration());
        reservationDTO.setStartAt(reservation.getStartAt());

        given(reservationService.getReservationById(reservation.getId()))
                .willReturn(reservationDTO);

        this.mockMvc.perform(get("/api/reservations/" + reservation.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.appointmentId", is(1)))
                .andExpect(jsonPath("$.clientId", is(1)))
                .andExpect(jsonPath("$.employeeId", is(1)));
    }

    @Test
    public void should_create_reservation() throws Exception {
        Reservation reservation = ReservationFactory.create();

        StoreReservationRequest request = new StoreReservationRequest();
        request.setReservationId(reservation.getId());
        request.setEmployeeId(1);
        request.setAppointmentId(1);
        request.setClientId(1);
        request.setStartAt("2020-01-01 14:00:00");

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmployeeId(1);
        reservationDTO.setAppointmentId(1);
        reservationDTO.setClientId(1);
        reservationDTO.setDuration(reservation.getDuration());
        reservationDTO.setStartAt(reservation.getStartAt());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        given(reservationService.storeReservation(reservationDTO))
                .willReturn(reservationDTO);

        this.mockMvc.perform(post("/api/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_update_reservation() throws Exception {
        Reservation reservation = ReservationFactory.create();

        StoreReservationRequest request = new StoreReservationRequest();
        request.setEmployeeId(1);
        request.setAppointmentId(1);
        request.setClientId(1);
        request.setStartAt("2020-01-01 14:00:00");

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmployeeId(1);
        reservationDTO.setAppointmentId(1);
        reservationDTO.setClientId(1);
        reservationDTO.setDuration(reservation.getDuration());
        reservationDTO.setStartAt(reservation.getStartAt());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        given(reservationService.storeReservation(reservationDTO))
                .willReturn(reservationDTO);

        System.out.println(json);

        this.mockMvc.perform(post("/api/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_delete_reservation_by_id() throws Exception {
        Reservation reservation = ReservationFactory.create();

        doNothing()
                .when(reservationService)
                .deleteReservation(reservation.getId());

        this.mockMvc.perform(delete("/api/reservations/" + reservation.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }
}

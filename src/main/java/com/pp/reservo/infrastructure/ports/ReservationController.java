package com.pp.reservo.infrastructure.ports;

import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.models.requests.CreateReservationRequest;
import com.pp.reservo.domain.models.response.BaseReservationsByClientResponseDTO;
import com.pp.reservo.domain.models.response.BaseReservationsByEmployeeResponseDTO;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api/reservations",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@Validated
public class ReservationController {

    private final ModelMapper modelMapper;
    private final ReservationService reservationService;

    public ReservationController(ModelMapper modelMapper, ReservationService reservationService) {
        this.modelMapper = modelMapper;
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationDTO> getReservations(@RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date byDate) {
        return reservationService.getAllReservations(byDate);
    }

    @RequestMapping(
            value = "/{reservationId}",
            method = RequestMethod.GET
    )
    public ReservationDTO getReservation(@PathVariable(value = "reservationId", required = false) Integer reservationId) throws EntityNotFoundException {
        return reservationService.getReservationById(reservationId);
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@Valid @RequestBody CreateReservationRequest reservation, BindingResult errors
    ) {
        this.reservationService
                .addReservation(this.modelMapper.map(reservation, ReservationDTO.class));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Integer reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    @RequestMapping(
            value = "/client/{clientId}",
            method = RequestMethod.GET
    )
    public BaseReservationsByClientResponseDTO getClientReservations(@PathVariable(value = "clientId", required = true) Integer clientId) {
        return reservationService.getReservationsByClient(clientId);
    }

    @RequestMapping(
            value = "/employee/{employeeId}",
            method = RequestMethod.GET
    )
    public BaseReservationsByEmployeeResponseDTO getEmployeeReservations(@PathVariable(value = "employeeId", required = true) Integer employeeId) {
        return reservationService.getReservationsByEmployee(employeeId);
    }
}

package com.pp.reservo.infrastructure.ports.http;

import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.models.requests.StoreAppointmentRequest;
import com.pp.reservo.infrastructure.services.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api/appointments",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class AppointmentController {

    private final ModelMapper modelMapper;
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentDTO> getAppointments() {
        return appointmentService.getAllAppointments();
    }

    @RequestMapping(
            value = "/{appointmentId}",
            method = RequestMethod.GET
    )
    public AppointmentDTO getAppointment(@PathVariable(value = "appointmentId", required = false) Integer appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    @PostMapping
    public ResponseEntity<Appointment> storeAppointment(@Valid @RequestBody StoreAppointmentRequest appointment) {
        this.appointmentService
                .storeAppointment(this.modelMapper.map(appointment, AppointmentDTO.class));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Integer appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
    }
}

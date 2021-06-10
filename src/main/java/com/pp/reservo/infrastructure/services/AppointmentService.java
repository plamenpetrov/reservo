package com.pp.reservo.infrastructure.services;

import com.pp.reservo.domain.dto.AppointmentDTO;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments();

    AppointmentDTO getAppointmentById(Integer appointmentId);

    AppointmentDTO storeAppointment(AppointmentDTO appointmentDTO);

    void deleteAppointment(Integer appointmentId);
}

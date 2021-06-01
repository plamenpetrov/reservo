package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.repositories.AppointmentRepository;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final ModelMapper modelMapper;
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(ModelMapper modelMapper, AppointmentRepository appointmentRepository) {
        this.modelMapper = modelMapper;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return this.appointmentRepository
                .findAll()
                .stream()
                .map(a -> this.modelMapper.map(a, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO getAppointmentById(Integer appointmentId) {
        return this.appointmentRepository
                .findById(appointmentId)
                .map(a -> this.modelMapper.map(a, AppointmentDTO.class))
                .orElseThrow(() ->
                        new EntityNotFoundException("Appointment with the given id was not found!"));
    }

    @Override
    public AppointmentDTO addAppointment(AppointmentDTO appointmentDTO) {
        this.appointmentRepository
                .saveAndFlush(this.modelMapper
                        .map(appointmentDTO, Appointment.class));

        return appointmentDTO;
    }

    @Override
    public void deleteAppointment(Integer appointmentId) {
        if(appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
        }
    }
}

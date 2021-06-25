package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.dto.event.appointment.AppointmentCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.appointment.AppointmentDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.appointment.AppointmentUpdatedDataEventDTO;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.events.publishers.AppointmentEventPublisher;
import com.pp.reservo.domain.repositories.AppointmentRepository;
import com.pp.reservo.domain.repositories.specification.AppointmentSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pp.reservo.domain.common.Domain.PAGE_SIZE;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final ModelMapper modelMapper;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentEventPublisher appointmentEventPublisher;

    public AppointmentServiceImpl(ModelMapper modelMapper,
                                  AppointmentRepository appointmentRepository,
                                  AppointmentEventPublisher appointmentEventPublisher
    ) {
        this.modelMapper = modelMapper;
        this.appointmentRepository = appointmentRepository;
        this.appointmentEventPublisher = appointmentEventPublisher;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(String byName, Integer fromDuration, Integer toDuration, Integer page, String sortBy) {
        PageRequest pageable = PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.Direction.ASC,
                sortBy
        );

        AppointmentSpecification specification = getSpecificationInstance(byName, fromDuration, toDuration);

        return this.appointmentRepository
                .findAll(specification, pageable)
                .stream()
                .map(a -> this.modelMapper.map(a, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    public AppointmentSpecification getSpecificationInstance(String byName, Integer fromDuration, Integer toDuration) {
        return new AppointmentSpecification(byName, fromDuration, toDuration);
    }

    @Override
    public AppointmentDTO getAppointmentById(Integer appointmentId) {
        Optional<Appointment> appointmentOptional = findById(appointmentId);

        if (appointmentOptional.isPresent()) {
            AppointmentDTO appointmentDTO= convertToDto(appointmentOptional.get());
            return appointmentDTO;
        }

        throw new EntityNotFoundException("Appointment with the given id was not found!");
    }

    private AppointmentDTO convertToDto(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setName(appointment.getName());
        appointmentDTO.setDuration(appointment.getDuration());

        return appointmentDTO;
    }

    private Appointment convertToEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDTO.getId());
        appointment.setName(appointmentDTO.getName());

        return appointment;
    }

    public Optional<Appointment> findById(Integer appointmentId) {
        return this.appointmentRepository.findById(appointmentId);
    }

    @Override
    public AppointmentDTO storeAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = convertToEntity(appointmentDTO);
        this.appointmentRepository.saveAndFlush(appointment);
        publishEventAppointmentStored(appointmentDTO);

        return appointmentDTO;
    }

    private void publishEventAppointmentStored(AppointmentDTO appointmentDTO) {
        if(appointmentDTO.getId() == null) {
            AppointmentCreatedDataEventDTO appointmentCreatedDataEventDTO = this.modelMapper.map(appointmentDTO, AppointmentCreatedDataEventDTO.class);
            appointmentEventPublisher.publishAppointmentStored(appointmentCreatedDataEventDTO);
        } else {
            AppointmentUpdatedDataEventDTO appointmentUpdatedDataEventDTO = this.modelMapper.map(appointmentDTO, AppointmentUpdatedDataEventDTO.class);
            appointmentEventPublisher.publishAppointmentStored(appointmentUpdatedDataEventDTO);
        }
    }

    private void publishEventAppointmentDelete(AppointmentDTO appointmentDTO) {
        BaseDataEventDTO appointmentDataEventDTO = this.modelMapper.map(appointmentDTO, AppointmentDeletedDataEventDTO.class);
        appointmentEventPublisher.publishAppointmentDeleted(appointmentDataEventDTO);
    }

    @Override
    public void deleteAppointment(Integer appointmentId) {
        if(appointmentRepository.existsById(appointmentId)) {
            AppointmentDTO appointmentDTO = getAppointmentById(appointmentId);
            appointmentRepository.deleteById(appointmentId);
            publishEventAppointmentDelete(appointmentDTO);
        }
    }
}

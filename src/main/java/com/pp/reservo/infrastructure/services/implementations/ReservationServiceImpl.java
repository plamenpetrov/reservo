package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.repositories.ReservationRepository;
import com.pp.reservo.domain.repositories.specification.ReservationSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ModelMapper modelMapper, ReservationRepository reservationRepository) {
        this.modelMapper = modelMapper;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<ReservationDTO> getAllReservations(Date byDate) {
        System.out.println(byDate);
        return this.reservationRepository
                .findAll(new ReservationSpecification(byDate))
                .stream()
                .map(r -> this.modelMapper.map(r, ReservationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO getReservationById(Integer reservationId) throws EntityNotFoundException {
        return this.reservationRepository
                .findById(reservationId)
                .map(r -> this.modelMapper.map(r, ReservationDTO.class))
                .orElseThrow(() ->
                        new EntityNotFoundException("Reservation with the given id was not found!"));
    }

    @Override
    public ReservationDTO addReservation(ReservationDTO reservationDTO) {
        this.reservationRepository
                .saveAndFlush(convertToEntity(reservationDTO));

        return reservationDTO;
    }

    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();

        reservation.setAppointmentId(reservationDTO.getAppointmentId());
        reservation.setClientId(reservationDTO.getClientId());
        reservation.setEmployeeId(reservationDTO.getClientId());
        reservation.setStartAt(reservationDTO.getStartAt());
        reservation.setDuration(reservationDTO.getDuration());
        reservation.setStartAt(reservationDTO.getStartAt());

        return reservation;
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        if(reservationRepository.existsById(reservationId)) {
            reservationRepository.deleteById(reservationId);
        }
    }
}

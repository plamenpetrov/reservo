package com.pp.reservo.infrastructure.services;

import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;

import java.util.List;

public interface ReservationService {
    List<ReservationDTO> getAllReservations();

    ReservationDTO getReservationById(Integer reservationId) throws EntityNotFoundException;

    ReservationDTO addReservation(ReservationDTO reservationDTO);

    void deleteReservation(Integer reservationId);
}

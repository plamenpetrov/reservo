package com.pp.reservo.infrastructure.services;

import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.models.response.BaseReservationsByClientResponseDTO;
import com.pp.reservo.domain.models.response.BaseReservationsByEmployeeResponseDTO;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;

import java.util.Date;
import java.util.List;

public interface ReservationService {
    List<ReservationDTO> getAllReservations(
            Date byDate,
            Integer clientId,
            Integer employeeId,
            Integer appointmentId,
            Integer page,
            String sortBy
    );

    ReservationDTO getReservationById(Integer reservationId) throws EntityNotFoundException;

    ReservationDTO storeReservation(ReservationDTO reservationDTO);

    void deleteReservation(Integer reservationId);

    BaseReservationsByClientResponseDTO getReservationsByClient(Integer clientId);

    BaseReservationsByEmployeeResponseDTO getReservationsByEmployee(Integer employeeId);
}

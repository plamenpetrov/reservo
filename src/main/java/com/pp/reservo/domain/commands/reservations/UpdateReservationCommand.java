package com.pp.reservo.domain.commands.reservations;


import com.pp.reservo.domain.common.Command;
import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.infrastructure.services.ReservationService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Setter
@Service("updateReservationCommand")
public class UpdateReservationCommand implements Command {

    private final ReservationService reservationService;
    private ReservationDTO reservationDTO;

    public UpdateReservationCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void execute() {
        reservationService.storeReservation(reservationDTO);
    }
}

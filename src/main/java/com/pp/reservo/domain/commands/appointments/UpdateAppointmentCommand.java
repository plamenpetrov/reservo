package com.pp.reservo.domain.commands.appointments;

import com.pp.reservo.domain.common.Command;
import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.infrastructure.services.AppointmentService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Getter
@Setter
@Service("updateAppointmentCommand")
public class UpdateAppointmentCommand implements Command {

    private final AppointmentService appointmentService;
    private AppointmentDTO appointmentDTO;

    public UpdateAppointmentCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void execute() {
        appointmentService.storeAppointment(appointmentDTO);
    }
}

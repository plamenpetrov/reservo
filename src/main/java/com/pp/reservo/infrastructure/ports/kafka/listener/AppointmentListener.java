package com.pp.reservo.infrastructure.ports.kafka.listener;

import com.pp.reservo.domain.commands.appointments.CreateAppointmentCommand;
import com.pp.reservo.domain.commands.appointments.DeleteAppointmentCommand;
import com.pp.reservo.domain.commands.appointments.UpdateAppointmentCommand;
import com.pp.reservo.domain.common.CommandExecutor;
import com.pp.reservo.domain.dto.AppointmentDTO;
import com.pp.reservo.domain.dto.event.appointment.AppointmentCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.appointment.AppointmentDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.appointment.AppointmentUpdatedDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.AppointmentEventSink;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableBinding(AppointmentEventSink.class)
public class AppointmentListener {
    private final ModelMapper modelMapper;

    @Autowired
    private final CommandExecutor commandExecutor;
    
    @Autowired
    private final CreateAppointmentCommand createAppointmentCommand;

    @Autowired
    private final UpdateAppointmentCommand updateAppointmentCommand;

    @Autowired
    private final DeleteAppointmentCommand deleteAppointmentCommand;

    @StreamListener(
            value = AppointmentEventSink.APPOINTMENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.createAppointment'"
    )
    public void onClientCreated(BaseEvent<AppointmentCreatedDataEventDTO> event) {
        AppointmentDTO appointmentDTO = this.modelMapper.map(event.getData(), AppointmentDTO.class);

        createAppointmentCommand.setAppointmentDTO(appointmentDTO);

        commandExecutor.executeCommand(createAppointmentCommand);
    }

    @StreamListener(
            value = AppointmentEventSink.APPOINTMENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.updateAppointment'"
    )
    public void onClientUpdated(BaseEvent<AppointmentUpdatedDataEventDTO> event) {
        AppointmentDTO appointmentDto = this.modelMapper.map(event.getData(), AppointmentDTO.class);

        updateAppointmentCommand.setAppointmentDTO(appointmentDto);

        commandExecutor.executeCommand(updateAppointmentCommand);
    }

    @StreamListener(
            value = AppointmentEventSink.APPOINTMENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.deleteAppointment'"
    )
    public void onClientDeleted(BaseEvent<AppointmentDeletedDataEventDTO> event) {
        deleteAppointmentCommand.setAppointmentId(event.getData().getAppointmentId());

        commandExecutor.executeCommand(deleteAppointmentCommand);
    }
}

package com.pp.reservo.infrastructure.ports.kafka.listener;

import com.pp.reservo.domain.commands.reservations.CreateReservationCommand;
import com.pp.reservo.domain.commands.reservations.DeleteReservationCommand;
import com.pp.reservo.domain.commands.reservations.UpdateReservationCommand;
import com.pp.reservo.domain.common.CommandExecutor;
import com.pp.reservo.domain.dto.ReservationDTO;
import com.pp.reservo.domain.dto.event.reservation.ReservationCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.reservation.ReservationDeletedEventDataDTO;
import com.pp.reservo.domain.dto.event.reservation.ReservationUpdatedDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.ReservationEventSink;
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
@EnableBinding(ReservationEventSink.class)
public class ReservationListener {

    private final ModelMapper modelMapper;

    @Autowired
    private final CommandExecutor commandExecutor;

    @Autowired
    private final CreateReservationCommand createReservationCommand;

    @Autowired
    private final UpdateReservationCommand updateReservationCommand;

    @Autowired
    private final DeleteReservationCommand deleteReservationCommand;

    @StreamListener(
            value = ReservationEventSink.RESERVATIONS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.createReservation'"
    )
    public void onReservationCreated(BaseEvent<ReservationCreatedDataEventDTO> event) {
        ReservationDTO reservationDTO = this.modelMapper.map(event.getData(), ReservationDTO.class);

        createReservationCommand.setReservationDTO(reservationDTO);

        commandExecutor.executeCommand(createReservationCommand);
    }

    @StreamListener(
            value = ReservationEventSink.RESERVATIONS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.updateReservation'"
    )
    public void onReservationUpdated(BaseEvent<ReservationUpdatedDataEventDTO> event) {
        ReservationDTO reservationDTO = this.modelMapper.map(event.getData(), ReservationDTO.class);

        updateReservationCommand.setReservationDTO(reservationDTO);

        commandExecutor.executeCommand(updateReservationCommand);
    }

    @StreamListener(
            value = ReservationEventSink.RESERVATIONS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.deleteReservation'"
    )
    public void onReservationDeleted(BaseEvent<ReservationDeletedEventDataDTO> event) {
        deleteReservationCommand.setReservationId(event.getData().getReservationId());

        commandExecutor.executeCommand(deleteReservationCommand);
    }
}

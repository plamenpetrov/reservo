package com.pp.reservo.infrastructure.ports.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ReservationEventSink {

    String RESERVATIONS_TOPIC_COMMANDS = "reservations-topic-commands";
    String RESERVATIONS_TOPIC_EVENTS = "reservations-topic-events";

    @Input(ReservationEventSink.RESERVATIONS_TOPIC_COMMANDS)
    MessageChannel onReservationCommand();

    @Output(ReservationEventSink.RESERVATIONS_TOPIC_EVENTS)
    MessageChannel onReservationEvent();
}

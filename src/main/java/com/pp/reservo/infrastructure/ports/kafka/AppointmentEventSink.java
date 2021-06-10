package com.pp.reservo.infrastructure.ports.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AppointmentEventSink {

    String APPOINTMENTS_TOPIC_COMMANDS = "appointments-topic-commands";
    String APPOINTMENTS_TOPIC_EVENTS = "appointments-topic-events";

    @Input(AppointmentEventSink.APPOINTMENTS_TOPIC_COMMANDS)
    MessageChannel onAppointmentCommand();

    @Output(AppointmentEventSink.APPOINTMENTS_TOPIC_EVENTS)
    MessageChannel onAppointmentEvent();
}

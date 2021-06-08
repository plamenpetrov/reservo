package com.pp.reservo.infrastructure.ports.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ClientsEventSink {

    String CLIENTS_TOPIC_COMMANDS = "clients-topic-commands";
    String CLIENTS_TOPIC_EVENTS = "clients-topic-events";

    @Input(ClientsEventSink.CLIENTS_TOPIC_COMMANDS)
    MessageChannel onClientCommand();

    @Output(ClientsEventSink.CLIENTS_TOPIC_EVENTS)
    MessageChannel onClientEvent();
}

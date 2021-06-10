package com.pp.reservo.infrastructure.ports.kafka.publisher;

import com.pp.reservo.infrastructure.ports.kafka.ClientsEventSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Slf4j
@EnableBinding(ClientsEventSink.class)
public class ClientsPublisher extends BasePublisher {

    public ClientsPublisher(ClientsEventSink eventSink) {
        super(eventSink.onClientEvent());
    }
}

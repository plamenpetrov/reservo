package com.pp.reservo.infrastructure.ports.kafka.publisher;

import com.pp.reservo.domain.dto.event.client.ClientCreatedDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.ClientsEventSink;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Slf4j
@EnableBinding(ClientsEventSink.class)
public class ClientsPublisher {

    private final MessageChannel messageChannel;

    public ClientsPublisher(ClientsEventSink eventSink) {
        this.messageChannel = eventSink.onClientEvent();
    }

    public void publishEvent(BaseEvent<ClientCreatedDataEventDTO> event) {
        Message<BaseEvent<ClientCreatedDataEventDTO>> message = MessageBuilder.withPayload(event).build();
        this.messageChannel.send(message);
        System.out.println(event);
    }
}

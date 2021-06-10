package com.pp.reservo.infrastructure.ports.kafka.publisher;

import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

abstract class BasePublisher {

    private final MessageChannel messageChannel;

    public BasePublisher(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    public void publishEvent(BaseEvent<BaseDataEventDTO> event) {
        Message<BaseEvent<BaseDataEventDTO>> message = MessageBuilder.withPayload(event).build();
        this.messageChannel.send(message);
        System.out.println(event);
    }
}

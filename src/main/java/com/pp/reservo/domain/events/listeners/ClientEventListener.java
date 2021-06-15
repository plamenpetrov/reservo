package com.pp.reservo.domain.events.listeners;

import com.pp.reservo.domain.events.BaseDataEvent;
import com.pp.reservo.infrastructure.ports.kafka.builders.BaseEventMessageBuilder;
import com.pp.reservo.infrastructure.ports.kafka.publisher.ClientsPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ClientEventListener implements ApplicationListener<BaseDataEvent> {

    private final BaseEventMessageBuilder eventMessageBuilder;
    private final ClientsPublisher clientsPublisher;

    public ClientEventListener(BaseEventMessageBuilder eventMessageBuilder, ClientsPublisher clientsPublisher) {
        this.eventMessageBuilder = eventMessageBuilder;
        this.clientsPublisher = clientsPublisher;
    }

    @Override
    public void onApplicationEvent(BaseDataEvent baseDataEvent) {
        clientsPublisher.publishEvent(eventMessageBuilder.buildMessage(baseDataEvent.getEventData()));
    }
}

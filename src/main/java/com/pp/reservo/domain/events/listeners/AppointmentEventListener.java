package com.pp.reservo.domain.events.listeners;

import com.pp.reservo.domain.events.BaseDataEvent;
import com.pp.reservo.infrastructure.ports.kafka.builders.BaseEventMessageBuilder;
import com.pp.reservo.infrastructure.ports.kafka.publisher.AppointmentPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener implements ApplicationListener<BaseDataEvent> {

    private final BaseEventMessageBuilder eventMessageBuilder;
    private final AppointmentPublisher appointmentPublisher;

    public AppointmentEventListener(BaseEventMessageBuilder eventMessageBuilder, AppointmentPublisher appointmentPublisher) {
        this.eventMessageBuilder = eventMessageBuilder;
        this.appointmentPublisher = appointmentPublisher;
    }

    @Override
    public void onApplicationEvent(BaseDataEvent baseDataEvent) {
        appointmentPublisher.publishEvent(eventMessageBuilder.buildMessage(baseDataEvent.getEventData()));
    }
}

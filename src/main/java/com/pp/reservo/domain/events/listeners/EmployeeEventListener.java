package com.pp.reservo.domain.events.listeners;

import com.pp.reservo.domain.events.BaseDataEvent;
import com.pp.reservo.infrastructure.ports.kafka.builders.BaseEventMessageBuilder;
import com.pp.reservo.infrastructure.ports.kafka.publisher.EmployeePublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEventListener implements ApplicationListener<BaseDataEvent> {

    private final BaseEventMessageBuilder eventMessageBuilder;
    private final EmployeePublisher employeePublisher;

    public EmployeeEventListener(BaseEventMessageBuilder eventMessageBuilder, EmployeePublisher employeePublisher) {
        this.eventMessageBuilder = eventMessageBuilder;
        this.employeePublisher = employeePublisher;
    }

    @Override
    public void onApplicationEvent(BaseDataEvent baseDataEvent) {
        employeePublisher.publishEvent(eventMessageBuilder.buildMessage(baseDataEvent.getEventData()));
    }
}

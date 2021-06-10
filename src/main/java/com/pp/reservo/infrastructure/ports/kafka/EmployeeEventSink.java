package com.pp.reservo.infrastructure.ports.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EmployeeEventSink {

    String EMPLOYEE_TOPIC_COMMANDS = "employee-topic-commands";
    String EMPLOYEE_TOPIC_EVENTS = "employee-topic-events";

    @Input(EmployeeEventSink.EMPLOYEE_TOPIC_COMMANDS)
    MessageChannel onEmployeeCommand();

    @Output(EmployeeEventSink.EMPLOYEE_TOPIC_EVENTS)
    MessageChannel onEmployeeEvent();
}

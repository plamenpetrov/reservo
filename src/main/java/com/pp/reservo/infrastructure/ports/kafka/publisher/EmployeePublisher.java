package com.pp.reservo.infrastructure.ports.kafka.publisher;

import com.pp.reservo.infrastructure.ports.kafka.EmployeeEventSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Slf4j
@EnableBinding(EmployeeEventSink.class)
public class EmployeePublisher extends BasePublisher {

    public EmployeePublisher(EmployeeEventSink eventSink) {
        super(eventSink.onEmployeeEvent());
    }

}

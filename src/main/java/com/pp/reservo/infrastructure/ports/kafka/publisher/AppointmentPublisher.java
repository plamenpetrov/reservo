package com.pp.reservo.infrastructure.ports.kafka.publisher;

import com.pp.reservo.infrastructure.ports.kafka.AppointmentEventSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Slf4j
@EnableBinding(AppointmentEventSink.class)
public class AppointmentPublisher extends BasePublisher {
    public AppointmentPublisher(AppointmentEventSink eventSink) {
        super(eventSink.onAppointmentEvent());
    }
}

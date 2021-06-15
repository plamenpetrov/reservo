package com.pp.reservo.domain.events.publishers;

import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.events.BaseDataEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishAppointmentStored(BaseDataEventDTO baseDataEventDTO) {
        BaseDataEvent baseDataEvent = new BaseDataEvent(this, baseDataEventDTO);
        this.applicationEventPublisher.publishEvent(baseDataEvent);
    }

    public void publishAppointmentDeleted(BaseDataEventDTO baseDataEventDTO) {
        BaseDataEvent baseDataEvent = new BaseDataEvent(this, baseDataEventDTO);
        this.applicationEventPublisher.publishEvent(baseDataEvent);
    }
}

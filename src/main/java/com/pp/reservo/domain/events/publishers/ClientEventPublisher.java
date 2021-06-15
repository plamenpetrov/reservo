package com.pp.reservo.domain.events.publishers;

import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.events.BaseDataEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ClientEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishClientStored(BaseDataEventDTO baseDataEventDTO) {
        BaseDataEvent baseDataEvent = new BaseDataEvent(this, baseDataEventDTO);
        this.applicationEventPublisher.publishEvent(baseDataEvent);
    }

    public void publishClientDeleted(BaseDataEventDTO baseDataEventDTO) {
        BaseDataEvent baseDataEvent = new BaseDataEvent(this, baseDataEventDTO);
        this.applicationEventPublisher.publishEvent(baseDataEvent);
    }
}

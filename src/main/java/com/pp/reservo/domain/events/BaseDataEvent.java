package com.pp.reservo.domain.events;

import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import org.springframework.context.ApplicationEvent;

public class BaseDataEvent extends ApplicationEvent {

    private final BaseDataEventDTO baseDataEventDTO;

    public BaseDataEvent(Object source, BaseDataEventDTO baseDataEventDTO) {
        super(source);
        this.baseDataEventDTO = baseDataEventDTO;
    }

    public BaseDataEventDTO getEventData() {
        return baseDataEventDTO;
    }
}

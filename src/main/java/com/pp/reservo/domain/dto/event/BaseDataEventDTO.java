package com.pp.reservo.domain.dto.event;

public interface BaseDataEventDTO<T> {
    String getEventType();
    String getEventSource();
}

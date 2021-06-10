package com.pp.reservo.infrastructure.ports.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent<T> {
    String id;
    String type;
    String source;
    String specversion;
    String time;
    String datacontenttype;
    T data;

    public T getData() {
        return data;
    }
}

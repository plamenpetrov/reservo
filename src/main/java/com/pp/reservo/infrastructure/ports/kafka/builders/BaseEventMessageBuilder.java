package com.pp.reservo.infrastructure.ports.kafka.builders;

import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@NoArgsConstructor
public class BaseEventMessageBuilder<T> {

    public BaseEvent<T> buildMessage(BaseDataEventDTO messageDto) {
        return new BaseEvent(
                "1.0",
                messageDto.getEventType(),
                messageDto.getEventSource(),
                UUID.randomUUID().toString(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()),
                "application/json",
                messageDto
        );
    }

//    public BaseEvent<ClientCreatedDataEventDTO> buildMessage(ClientCreatedDataEventDTO clientData) {
//        return new BaseEvent<>(
//                "1.0",
//                "com.pp.reservo.clientCreated",
//                "/api/clients",
//                UUID.randomUUID().toString(),
//                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()),
//                "application/json",
//                clientData
//        );
//    }
}

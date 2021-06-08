package com.pp.reservo.infrastructure.ports.kafka.listener;

import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.dto.event.ClientCreatedDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.ClientsEventSink;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import com.pp.reservo.infrastructure.ports.kafka.publisher.ClientsPublisher;
import com.pp.reservo.infrastructure.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableBinding(ClientsEventSink.class)
public class ClientsListener {

    private final ModelMapper modelMapper;
    private final ClientService clientService;

    @Autowired
    private ClientsPublisher clientsPublisher;

    @StreamListener(
            value = ClientsEventSink.CLIENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.createClient'"
    )
    public void onClientCreated(BaseEvent<ClientCreatedDataEventDTO> event) {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setName(event.getData().getName());

        this.clientService.addClient(clientDto);

        ClientCreatedDataEventDTO clientCreatedDataEventDTO = this.modelMapper.map(clientDto, ClientCreatedDataEventDTO.class);

        clientsPublisher.publishClientCreated(buildMessage(clientCreatedDataEventDTO));
    }

    private BaseEvent<ClientCreatedDataEventDTO> buildMessage(ClientCreatedDataEventDTO clientData) {
        return new BaseEvent<>(
                "1.0",
                "com.pp.reservo.clientCreated",
                "/api/clients",
                UUID.randomUUID().toString(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()),
                "application/json",
                clientData
        );
    }
}

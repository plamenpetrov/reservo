package com.pp.reservo.infrastructure.ports.kafka.listener;

import com.pp.reservo.domain.commands.clients.CreateClientCommand;
import com.pp.reservo.domain.commands.clients.DeleteClientCommand;
import com.pp.reservo.domain.commands.clients.UpdateClientCommand;
import com.pp.reservo.domain.common.CommandExecutor;
import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.dto.event.client.ClientCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientUpdatedDataEventDTO;
import com.pp.reservo.infrastructure.ports.kafka.ClientsEventSink;
import com.pp.reservo.infrastructure.ports.kafka.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableBinding(ClientsEventSink.class)
public class ClientsListener {

    private final ModelMapper modelMapper;

    @Autowired
    private final CommandExecutor commandExecutor;

    @Autowired
    private final CreateClientCommand createClientCommand;

    @Autowired
    private final UpdateClientCommand updateClientCommand;

    @Autowired
    private final DeleteClientCommand deleteClientCommand;

    @StreamListener(
            value = ClientsEventSink.CLIENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.createClient'"
    )
    public void onClientCreated(BaseEvent<ClientCreatedDataEventDTO> event) {
        ClientDTO clientDto = this.modelMapper.map(event.getData(), ClientDTO.class);

        createClientCommand.setClientDto(clientDto);

        commandExecutor.executeCommand(createClientCommand);
    }

    @StreamListener(
            value = ClientsEventSink.CLIENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.updateClient'"
    )
    public void onClientUpdated(BaseEvent<ClientUpdatedDataEventDTO> event) {
        ClientDTO clientDto = this.modelMapper.map(event.getData(), ClientDTO.class);

        updateClientCommand.setClientDto(clientDto);

        commandExecutor.executeCommand(updateClientCommand);
    }

    @StreamListener(
            value = ClientsEventSink.CLIENTS_TOPIC_COMMANDS,
            condition = "payload.type matches 'com.pp.reservo.deleteClient'"
    )
    public void onClientDeleted(BaseEvent<ClientDeletedDataEventDTO> event) {
        deleteClientCommand.setClientId(event.getData().getClientId());

        commandExecutor.executeCommand(deleteClientCommand);
    }
}

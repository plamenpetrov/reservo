package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.dto.event.client.ClientCreatedDataEventDTO;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.repositories.ClientRepository;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.ports.kafka.builders.BaseEventMessageBuilder;
import com.pp.reservo.infrastructure.ports.kafka.publisher.ClientsPublisher;
import com.pp.reservo.infrastructure.services.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final ClientsPublisher clientsPublisher;
    private final BaseEventMessageBuilder eventMessageBuilder;

    public ClientServiceImpl(ModelMapper modelMapper, ClientRepository clientRepository, ClientsPublisher clientsPublisher, BaseEventMessageBuilder eventMessageBuilder) {
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.clientsPublisher = clientsPublisher;
        this.eventMessageBuilder = eventMessageBuilder;
    }

    public List<ClientDTO> getAllClients() {
        return this.clientRepository
                .findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, ClientDTO.class))
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Integer clientId) {
        return this.clientRepository
                .findById(clientId)
                .map(c -> this.modelMapper.map(c, ClientDTO.class))
                .orElseThrow(() ->
                        new EntityNotFoundException("Client with the given id was not found!"));
    }

    public ClientDTO storeClient(ClientDTO clientDTO) {
        this.clientRepository
                .saveAndFlush(this.modelMapper
                        .map(clientDTO, Client.class));

        ClientCreatedDataEventDTO clientCreatedDataEventDTO = this.modelMapper.map(clientDTO, ClientCreatedDataEventDTO.class);

        clientsPublisher.publishClientCreated(eventMessageBuilder.buildMessage(clientCreatedDataEventDTO));

        return clientDTO;
    }

    public void deleteClient(Integer clientId) {
        if(clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        }
    }
}

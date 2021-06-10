package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientUpdatedDataEventDTO;
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

        publishEventClientChange(clientDTO);

        return clientDTO;
    }

    private void publishEvent(BaseDataEventDTO eventDataEventDTO) {
        clientsPublisher.publishEvent(eventMessageBuilder.buildMessage(eventDataEventDTO));
    }

    private void publishEventClientChange(ClientDTO clientDTO) {
        BaseDataEventDTO clientDataEventDTO = mapDataDTO(clientDTO);
        publishEvent(clientDataEventDTO);
    }

    private BaseDataEventDTO mapDataDTO(ClientDTO clientDTO) {
        if(clientDTO.getId() == null) {
            return this.modelMapper.map(clientDTO, ClientCreatedDataEventDTO.class);
        } else {
            return this.modelMapper.map(clientDTO, ClientUpdatedDataEventDTO.class);
        }
    }

    private void publishEventClientDelete(ClientDTO clientDTO) {
        BaseDataEventDTO clientDataEventDTO = this.modelMapper.map(clientDTO, ClientDeletedDataEventDTO.class);
        publishEvent(clientDataEventDTO);
    }

    public void deleteClient(Integer clientId) {
        if(clientRepository.existsById(clientId)) {
            ClientDTO clientDTO = getClientById(clientId);
            clientRepository.deleteById(clientId);
            publishEventClientDelete(clientDTO);
        }
    }
}

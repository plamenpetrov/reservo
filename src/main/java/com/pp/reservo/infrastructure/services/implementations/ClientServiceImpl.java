package com.pp.reservo.infrastructure.services.implementations;

import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientCreatedDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientDeletedDataEventDTO;
import com.pp.reservo.domain.dto.event.client.ClientUpdatedDataEventDTO;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.events.publishers.ClientEventPublisher;
import com.pp.reservo.domain.repositories.ClientRepository;
import com.pp.reservo.domain.repositories.specification.ClientSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pp.reservo.domain.common.Domain.PAGE_SIZE;

@Service
public class ClientServiceImpl implements ClientService {

    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final ClientEventPublisher clientEventPublisher;

    public ClientServiceImpl(ModelMapper modelMapper,
                             ClientRepository clientRepository,
                             ClientEventPublisher clientEventPublisher
    ) {
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.clientEventPublisher = clientEventPublisher;
    }

    public List<ClientDTO> getAllClients(String byName, Integer page, String sortBy) {
        return this.clientRepository
                .findAll(new ClientSpecification(
                    byName
                ),

                PageRequest.of(
                        page,
                        PAGE_SIZE,
                        Sort.Direction.ASC,
                        sortBy
                ))
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

        publishEventClientStored(clientDTO);

        return clientDTO;
    }

    private void publishEventClientStored(ClientDTO clientDTO) {
        if(clientDTO.getId() == null) {
            ClientCreatedDataEventDTO clientCreatedDataEventDTO = this.modelMapper.map(clientDTO, ClientCreatedDataEventDTO.class);
            clientEventPublisher.publishClientStored(clientCreatedDataEventDTO);
        } else {
            ClientUpdatedDataEventDTO updatedDataEventDTO = this.modelMapper.map(clientDTO, ClientUpdatedDataEventDTO.class);
            clientEventPublisher.publishClientStored(updatedDataEventDTO);
        }
    }

    private void publishEventClientDelete(ClientDTO clientDTO) {
        BaseDataEventDTO clientDataEventDTO = this.modelMapper.map(clientDTO, ClientDeletedDataEventDTO.class);
        clientEventPublisher.publishClientDeleted(clientDataEventDTO);
    }

    public void deleteClient(Integer clientId) {
        if(clientRepository.existsById(clientId)) {
            ClientDTO clientDTO = getClientById(clientId);
            clientRepository.deleteById(clientId);
            publishEventClientDelete(clientDTO);
        }
    }
}

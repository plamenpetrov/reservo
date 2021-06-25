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
import java.util.Optional;
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
        PageRequest pageable = PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.Direction.ASC,
                sortBy
        );

        ClientSpecification specification = getSpecificationInstance(byName);

        return this.clientRepository
                .findAll(specification, pageable)
                .stream()
                .map(c -> this.modelMapper.map(c, ClientDTO.class))
                .collect(Collectors.toList());
    }

    public ClientSpecification getSpecificationInstance(String byName) {
        return new ClientSpecification(byName);
    }

    public ClientDTO getClientById(Integer clientId) {
        Optional<Client> clientOptional = findById(clientId);

        if (clientOptional.isPresent()) {
            ClientDTO clientDTO = convertToDto(clientOptional.get());
            return clientDTO;
        }

        throw new EntityNotFoundException("Client with the given id was not found!");
    }

    private ClientDTO convertToDto(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());

        return clientDTO;
    }

    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());

        return client;
    }

    public Optional<Client> findById(Integer clientId) {
        return this.clientRepository.findById(clientId);
    }

    public ClientDTO storeClient(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        this.clientRepository.saveAndFlush(client);
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

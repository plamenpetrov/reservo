package com.pp.reservo.infrastructure.services;

import com.pp.reservo.domain.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getAllClients();

    ClientDTO getClientById(Integer clientId);

    ClientDTO storeClient(ClientDTO clientDTO);

    void deleteClient(Integer clientId);
}

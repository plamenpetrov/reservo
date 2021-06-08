package com.pp.reservo.infrastructure.ports.http;

import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.models.requests.StoreClientRequest;
import com.pp.reservo.infrastructure.services.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api/clients",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class ClientsController {

    private final ModelMapper modelMapper;
    private final ClientService clientService;

    public ClientsController(ClientService clientService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientDTO> getClients() {
        return clientService.getAllClients();
    }

    @RequestMapping(
            value = "/{clientId}",
            method = RequestMethod.GET
    )
    public ClientDTO getClient(@PathVariable(value = "clientId", required = false) Integer clientId) {
        return clientService.getClientById(clientId);
    }

    @PostMapping
    public ResponseEntity<Client> storeClient(@Valid @RequestBody StoreClientRequest client) {
        this.clientService
                .storeClient(this.modelMapper.map(client, ClientDTO.class));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Integer clientId) {
            clientService.deleteClient(clientId);
    }
}
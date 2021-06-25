package com.pp.reservo.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.infrastructure.ports.http.ClientsController;
import com.pp.reservo.infrastructure.services.ClientService;
import com.pp.reservo.unit.factories.ClientFactory;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.pp.reservo.domain.common.Domain.DEFAULT_PAGE;
import static com.pp.reservo.domain.common.Domain.DEFAULT_SORT_COLUMN;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureJsonTesters
@WebMvcTest(controllers = ClientsController.class)
public class ClientsControllerTest {

    @Value("${reservo.auth.basic.username}")
    protected String basicAuthUsername;

    @Value("${reservo.auth.basic.password}")
    protected String basicAuthPassword;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_all_clients_without_filters() throws Exception {
        List<ClientDTO> clients = new ArrayList<>();

        Client client = ClientFactory.create();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());

        clients.add(clientDTO);

        given(clientService.getAllClients(null, DEFAULT_PAGE, DEFAULT_SORT_COLUMN))
                .willReturn(clients);

        this.mockMvc.perform(get("/api/clients")
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(client.getId())))
                .andExpect(jsonPath("$[0].name", is(client.getName())));
    }

    @Test
    public void should_return_error_if_no_authentication() throws Exception {
        this.mockMvc.perform(get("/api/clients"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_return_client_by_id() throws Exception {
        Client client = ClientFactory.create();
        client.setId(1);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());

        given(clientService.getClientById(client.getId()))
                .willReturn(clientDTO);

        this.mockMvc.perform(get("/api/clients/" + client.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(client.getId())))
                .andExpect(jsonPath("$.name", is(client.getName())));
    }

    @Test
    public void should_create_client() throws Exception {
        Client client = ClientFactory.create();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName(client.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(client);

        given(clientService.storeClient(clientDTO))
                .willReturn(clientDTO);

        this.mockMvc.perform(post("/api/clients/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_update_client() throws Exception {
        Client client = ClientFactory.create();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(client);

        given(clientService.storeClient(clientDTO))
                .willReturn(clientDTO);

        this.mockMvc.perform(post("/api/clients/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_delete_client_by_id() throws Exception {
        Client client = ClientFactory.create();
        client.setId(1);

        doNothing()
                .when(clientService)
                .deleteClient(client.getId());

        this.mockMvc.perform(delete("/api/clients/" + client.getId())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(basicAuthUsername, basicAuthPassword)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }
}

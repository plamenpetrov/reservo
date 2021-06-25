package com.pp.reservo.unit.services;

import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.events.publishers.ClientEventPublisher;
import com.pp.reservo.domain.repositories.ClientRepository;
import com.pp.reservo.domain.repositories.specification.ClientSpecification;
import com.pp.reservo.infrastructure.exceptions.EntityNotFoundException;
import com.pp.reservo.infrastructure.services.implementations.ClientServiceImpl;
import com.pp.reservo.unit.factories.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.pp.reservo.domain.common.Domain.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientEventPublisher clientEventPublisher;

    @InjectMocks
    private ClientServiceImpl clientService;

    private List<Client> clientEntities;

    @BeforeEach
    public void initService() {
        clientService = new ClientServiceImpl(modelMapper, clientRepository, clientEventPublisher);
        clientEntities = initClients();
    }

    private List<Client> initClients() {
        List<Client> clientEntities = new ArrayList<>();
        clientEntities.add(ClientFactory.create());
        clientEntities.add(ClientFactory.create());

        return clientEntities;
    }
    
    @ParameterizedTest
    @MethodSource("allClientsDataProvider")
    public void should_return_all_clients_without_filters(String byName, Integer page, String sortBy, Integer expected) {
        testGetClients(byName, page, sortBy, expected);
    }

    @Test
    public void should_return_clients_filtered_by_name() {
        Client client = clientEntities.get(0);
        String byName = client.getName();
        clientEntities.remove(0);

        testGetClients(byName, DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 1);
    }

    @Test
    public void should_not_return_clients_filtered_by_name() {
        clientEntities.clear();
        testGetClients("testClient", DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 0);
    }

    private void testGetClients(String byName, Integer page, String sortBy, Integer expected) {
        int size = PAGE_SIZE;

        Page<Client> paginatedClients = new PageImpl(clientEntities);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);

        ClientSpecification clientSpecification = new ClientSpecification(byName);

        ClientServiceImpl clientServiceSpy = Mockito.spy(clientService);

        doReturn(clientSpecification)
                .when(clientServiceSpy)
                .getSpecificationInstance(byName);

        when(clientRepository.findAll(clientSpecification, pageable))
                .thenReturn(paginatedClients);

        List<ClientDTO> clientsDB = clientServiceSpy.getAllClients(byName, page, sortBy);

        assertEquals(expected, clientsDB.size());
    }

    public static Stream<Arguments> allClientsDataProvider() {
        return Stream.of(
                Arguments.of("", DEFAULT_PAGE, DEFAULT_SORT_COLUMN, 2),
                Arguments.of("", DEFAULT_PAGE, "name", 2),
                Arguments.of("", 2, "name", 2)
        );
    }

    @Test
    public void should_find_existing_client() throws EntityNotFoundException {
        Client client = ClientFactory.create();
        Integer clientId = client.getId();

        ClientServiceImpl clientServiceSpy = Mockito.spy(clientService);

        doReturn(Optional.of(client))
                .when(clientServiceSpy)
                .findById(clientId);

        ClientDTO clientsDB = clientServiceSpy.getClientById(clientId);

        assertEquals(client.getName(), clientsDB.getName());
        assertEquals(client.getId(), clientsDB.getId());
    }

    @Test
    public void should_store_new_client() {
        Client client = new Client();
        client.setName(client.getName());

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName(client.getName());

        ClientServiceImpl clientServiceSpy = Mockito.spy(clientService);

        doReturn(client)
                .when(clientRepository)
                .saveAndFlush(client);

        ClientDTO storedClient = clientServiceSpy.storeClient(clientDTO);

        assertEquals(clientDTO, storedClient);
    }

    @Test
    public void should_delete_existing_client() {
        Client client = clientEntities.get(0);
        Integer clientId = client.getId();

        when(clientRepository.existsById(clientId))
                .thenReturn(true);

        ClientDTO clientDTO = this.modelMapper.map(client, ClientDTO.class);

        ClientServiceImpl clientServiceSpy = Mockito.spy(clientService);

        doReturn(clientDTO)
                .when(clientServiceSpy)
                .getClientById(clientId);

        clientServiceSpy.deleteClient(clientId);

        Mockito.verify(clientRepository, Mockito.times(1)).deleteById(clientId);
    }
}

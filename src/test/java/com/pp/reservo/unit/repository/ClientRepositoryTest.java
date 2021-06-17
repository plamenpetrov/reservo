package com.pp.reservo.unit.repository;

import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.repositories.ClientRepository;
import com.pp.reservo.unit.factories.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private ClientFactory clientFactory;

    @BeforeEach
    public void init() {
        this.clientFactory = new ClientFactory();
    }

    @Test
    public void findAllClients() {
        clientRepository.save(clientFactory.create());
        assertEquals(1, clientRepository.findAll().size());
    }

    @Test
    public void findClientById() {
        Client client = clientRepository.save(clientFactory.create());

        Client clientDB = clientRepository.getById(client.getId());
        assertNotNull(clientDB);
    }

    @Test
    public void createClient() {
        Client client = clientFactory.create();

        Client clientDB = clientRepository.save(client);

        assertEquals(client.getName(), clientDB.getName());
    }

    @Test
    public void deleteClient() {
        Client client = clientRepository.save(clientFactory.create());

        clientRepository.deleteById(client.getId());

        Optional<Client> clientDB = clientRepository.findById(client.getId());

        assertThat(clientDB.isEmpty());
    }
}

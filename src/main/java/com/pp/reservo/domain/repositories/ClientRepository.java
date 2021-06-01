package com.pp.reservo.domain.repositories;

import com.pp.reservo.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findClientById(Integer clientId);
}

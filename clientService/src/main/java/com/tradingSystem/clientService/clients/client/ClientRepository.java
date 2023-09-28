package com.tradingSystem.clientService.clients.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByEmail(String email);
//    Optional<Client> findByRole();
    List<Client> findAllByRole(Role role);
    Client findUserByEmailIs(String email);

}

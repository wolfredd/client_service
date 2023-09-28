package com.tradingSystem.clientService.clients.administrator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    Optional<Administrator> findByEmail(String email);
    Administrator findUserByEmailIs(String email);

}

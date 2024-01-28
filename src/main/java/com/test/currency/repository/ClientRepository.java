package com.test.currency.repository;

import com.test.currency.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findById(Long aLong);

    Optional<Client> findByAccountNumber(String accountNumber);

}

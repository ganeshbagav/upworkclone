package com.incubator.upwork.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Client;

public interface ClientRepository extends JpaRepository<Client,Integer> {

    Client findByEmail(String email);
    Optional<Client> findByPhone(String phone);
    
    boolean existsByEmail(String email);
}

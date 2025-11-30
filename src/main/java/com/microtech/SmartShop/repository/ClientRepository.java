package com.microtech.SmartShop.repository;

import com.microtech.SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUsername(String username);
//    boolean existsByEmail(String email);
    Optional<Client> existsByEmail(String email);

}

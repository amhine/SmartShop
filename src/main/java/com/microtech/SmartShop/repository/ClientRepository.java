package com.microtech.SmartShop.repository;

import com.microtech.SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

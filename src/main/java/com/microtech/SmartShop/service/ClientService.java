package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;

import java.util.Map;

public interface ClientService {
    Client createClient(Client client);

    ClientDTO findById(Long id);

    void deleteClient(Long id);
}

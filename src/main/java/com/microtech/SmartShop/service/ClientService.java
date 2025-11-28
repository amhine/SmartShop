package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.User;

import java.util.List;
import java.util.Map;

public interface ClientService {
    Client createClient(Client client);

    ClientDTO findById(Long id);

    void deleteClient(Long id);

    List<CommandeDTO> getCommandes(Long clientId);

    ClientDTO updateClient(Long id, ClientDTO clientDTO, User currentUser);

}

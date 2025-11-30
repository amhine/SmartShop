package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.ClientCreateDto;
import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;

import java.util.List;

public interface ClientService {
     ClientDTO createClient(ClientCreateDto dto);
     ClientDTO getClient(Long id) ;
     Client getClientEntity(Long id) ;
     ClientDTO updateClient(Long id, ClientCreateDto dto);
     void deleteClient(Long id);
     List<CommandeDTO> getCommandes(Long clientId);
    }

package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.CommandeCreateDto;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;

public interface CommandeService {
    void updateStats (Client client);
    CommandeDTO getCommande(Long id) ;
    Commande getCommandeEntity(Long id);
    CommandeDTO confirmCommande(Long id);
    CommandeDTO createCommande(CommandeCreateDto dto);
}

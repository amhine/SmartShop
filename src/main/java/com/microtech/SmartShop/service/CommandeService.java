package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.CommandeCreateDto;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;

import java.util.List;

public interface CommandeService {

    void updateStats(Client client);
    List<CommandeDTO> getCommandes();
    Commande getCommandeEntity(Long id);
    CommandeDTO getCommande(Long id);
    CommandeDTO confirmCommande(Long id);
    CommandeDTO createCommande(CommandeCreateDto dto);
    CommandeDTO cancelCommande(Long id);
    void deleteCommande(Long id);
}




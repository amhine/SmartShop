package com.microtech.SmartShop.service;

import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;

public interface CommandeService {
    void updateStats (Client client);
    Commande confirmCommande(Long commandeId);
}

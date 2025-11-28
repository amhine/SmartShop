package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.OrderStatus;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandeServiceImpl implements CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void updateStats(Client client){
        long totalOrders = commandeRepository.countByClientAndStatut(client, OrderStatus.Confirmed);
        double totalSpent = commandeRepository.sumTotalTTCByClientAndStatut(client, OrderStatus.Confirmed);

        if (totalOrders >= 20 || totalSpent >= 15000) {
            client.setCustomer(CustomerTier.Platinum);
        } else if (totalOrders >= 10 || totalSpent >= 5000) {
            client.setCustomer(CustomerTier.Gold);
        } else if (totalOrders >= 3 || totalSpent >= 1000) {
            client.setCustomer(CustomerTier.Silver);
        } else {
            client.setCustomer(CustomerTier.Basic);
        }

        clientRepository.save(client);
    }

    }


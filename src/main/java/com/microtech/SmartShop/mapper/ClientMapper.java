package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDTO toDto(Client client){
        if(client == null) return null;

        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setNiveauFidelite(client.getCustomer());

        dto.setTotalOrders(client.getTotalOrders());
        dto.setTotalSpent(client.getTotalSpent());

        dto.setFirstOrderDate(
                client.getFirstOrderDate() != null ? client.getFirstOrderDate().toLocalDate() : null
        );
        dto.setLastOrderDate(
                client.getLastOrderDate() != null ? client.getLastOrderDate().toLocalDate() : null
        );

        return dto;
    }

    public Client toEntity(ClientDTO dto){
        if(dto == null) return null;

        Client client = new Client();
        client.setId(dto.getId());
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        client.setCustomer(dto.getNiveauFidelite());

        return client;
    }
}

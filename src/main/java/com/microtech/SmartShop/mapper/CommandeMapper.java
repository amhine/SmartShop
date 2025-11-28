package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Commande;

public class CommandeMapper {

    public static CommandeDTO toDTO(Commande commande) {
        if (commande == null) return null;

        return CommandeDTO.builder()
                .id(commande.getId())
                .dateCreation(commande.getDateCreation())
                .totalTTC(commande.getTotalTTC())
                .statut(commande.getStatut())
                .build();
    }
}

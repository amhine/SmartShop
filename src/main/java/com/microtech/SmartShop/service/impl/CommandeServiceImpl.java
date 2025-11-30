package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.OrderItem;
import com.microtech.SmartShop.entity.Product;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.OrderStatus;
import com.microtech.SmartShop.mapper.CommandeMapper;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.repository.ProductRepository;
import com.microtech.SmartShop.service.CommandeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final CommandeMapper commandeMapper;
    private final ProductRepository productRepository;

    @Override
    public void updateStats(Client client) {
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

    @Override
    public CommandeDTO getCommande(Long id) {
        return commandeRepository.findById(id)
                .map(commandeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    @Override
    public Commande getCommandeEntity(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    @Override
    @Transactional
    public CommandeDTO confirmCommande(Long id) {

        Commande commande = getCommandeEntity(id);

        if (commande.getStatut() != OrderStatus.Pending) {
            throw new RuntimeException("La commande doit être en statut PENDING");
        }

        if (commande.getMontantRestant() > 0) {
            throw new RuntimeException("Commande non totalement payée");
        }
        for (OrderItem item : commande.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantite()) {
                commande.setStatut(OrderStatus.Rejeted);
                commandeRepository.save(commande);
                throw new RuntimeException("Stock insuffisant pour : " + product.getNom());
            }
            product.setStock(product.getStock() - item.getQuantite());
            productRepository.save(product);
        }
        applyDiscount(commande);
        commande.setStatut(OrderStatus.Confirmed);
        commandeRepository.save(commande);
        updateStats(commande.getClient());

        return commandeMapper.toDto(commande);
    }

    private void applyDiscount(Commande commande) {
        Client client = commande.getClient();
        CustomerTier tier = client.getCustomer();

        BigDecimal sousTotal = BigDecimal.valueOf(commande.getSousTotalHT());
        BigDecimal discountRate;

        switch (tier) {
            case Platinum -> discountRate = BigDecimal.valueOf(0.20);
            case Gold -> discountRate = BigDecimal.valueOf(0.15);
            case Silver -> discountRate = BigDecimal.valueOf(0.10);
            default -> discountRate = BigDecimal.ZERO;
        }

        BigDecimal montantRemise = sousTotal.multiply(discountRate);
        BigDecimal montantHTApresRemise = sousTotal.subtract(montantRemise);
        BigDecimal montantTVA = montantHTApresRemise.multiply(BigDecimal.valueOf(0.2));
        BigDecimal totalTTC = montantHTApresRemise.add(montantTVA);

        commande.setMontantRemise(montantRemise.doubleValue());
        commande.setMontantHTApresRemise(montantHTApresRemise.doubleValue());
        commande.setMontantTVA(montantTVA.doubleValue());
        commande.setTotalTTC(totalTTC.doubleValue());
    }
}

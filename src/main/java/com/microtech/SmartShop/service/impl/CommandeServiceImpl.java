package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.CommandeCreateDto;
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
        BigDecimal totalSpent = BigDecimal.valueOf(
                commandeRepository.sumTotalTTCByClientAndStatut(client, OrderStatus.Confirmed)
        );

        if (totalOrders >= 20 || totalSpent.compareTo(BigDecimal.valueOf(15000)) >= 0) {
            client.setCustomer(CustomerTier.Platinum);
        } else if (totalOrders >= 10 || totalSpent.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            client.setCustomer(CustomerTier.Gold);
        } else if (totalOrders >= 3 || totalSpent.compareTo(BigDecimal.valueOf(1000)) >= 0) {
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

        if (commande.getMontantRestant().compareTo(BigDecimal.ZERO) > 0) {
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

        BigDecimal sousTotal = commande.getSousTotalHT();

        if (sousTotal.compareTo(BigDecimal.valueOf(500)) < 0) {
            commande.setMontantRemise(BigDecimal.ZERO);
            commande.setMontantHTApresRemise(sousTotal);
            BigDecimal tva = sousTotal.multiply(BigDecimal.valueOf(0.20));
            commande.setMontantTVA(tva);
            commande.setTotalTTC(sousTotal.add(tva));
            return;
        }

        CustomerTier tier = commande.getClient().getCustomer();

        BigDecimal discountRate = switch (tier) {
            case Platinum -> BigDecimal.valueOf(0.20);
            case Gold -> BigDecimal.valueOf(0.15);
            case Silver -> BigDecimal.valueOf(0.10);
            default -> BigDecimal.ZERO;
        };

        BigDecimal remise = sousTotal.multiply(discountRate);
        BigDecimal htApresRemise = sousTotal.subtract(remise);
        BigDecimal tva = htApresRemise.multiply(BigDecimal.valueOf(0.20));
        BigDecimal totalTTC = htApresRemise.add(tva);

        commande.setMontantRemise(remise);
        commande.setMontantHTApresRemise(htApresRemise);
        commande.setMontantTVA(tva);
        commande.setTotalTTC(totalTTC);
    }

    @Override
    @Transactional
    public CommandeDTO createCommande(CommandeCreateDto dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setStatut(OrderStatus.Pending);

        BigDecimal sousTotalHT = BigDecimal.ZERO;
        for (var itemDto : dto.getItems()) {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            if (product.getStock() < itemDto.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour : " + product.getNom());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setCommande(commande);
            orderItem.setProduct(product);
            orderItem.setQuantite(itemDto.getQuantite());
            orderItem.setPrixUnitaire(product.getPrixUnitaire());

            BigDecimal totalLigne = product.getPrixUnitaire().multiply(
                    BigDecimal.valueOf(itemDto.getQuantite())
            );

            orderItem.setTotalLigne(totalLigne);

            commande.getItems().add(orderItem);

            sousTotalHT = sousTotalHT.add(totalLigne);
        }

        commande.setSousTotalHT(sousTotalHT);

        BigDecimal montantRemisePromo = BigDecimal.ZERO;
        if (dto.getCodePromo() != null && !dto.getCodePromo().isBlank()) {

            if (!dto.getCodePromo().matches("PROMO-[A-Z0-9]{4}")) {
                throw new RuntimeException("Code promo invalide");
            }

            montantRemisePromo = sousTotalHT.multiply(BigDecimal.valueOf(0.10));
            commande.setCodePromo(dto.getCodePromo());
        }

        BigDecimal tauxRemiseFidelite = switch (client.getCustomer()) {
            case Platinum -> BigDecimal.valueOf(0.20);
            case Gold -> BigDecimal.valueOf(0.15);
            case Silver -> BigDecimal.valueOf(0.10);
            default -> BigDecimal.ZERO;
        };

        BigDecimal montantRemiseFidelite = sousTotalHT.multiply(tauxRemiseFidelite);
        BigDecimal montantRemise = montantRemiseFidelite.add(montantRemisePromo);

        commande.setMontantRemise(montantRemise);

        BigDecimal montantHTApresRemise = sousTotalHT.subtract(montantRemise);
        commande.setMontantHTApresRemise(montantHTApresRemise);

        BigDecimal montantTVA = montantHTApresRemise.multiply(BigDecimal.valueOf(0.20));
        commande.setMontantTVA(montantTVA);

        BigDecimal totalTTC = montantHTApresRemise.add(montantTVA);
        commande.setTotalTTC(totalTTC);

        commande.setMontantRestant(totalTTC);

        for (OrderItem item : commande.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantite());
            productRepository.save(product);
        }

        Commande saved = commandeRepository.save(commande);
        return commandeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CommandeDTO cancelCommande(Long id) {

        Commande commande = getCommandeEntity(id);
        if (commande.getStatut() != OrderStatus.Pending) {
            throw new RuntimeException("Seules les commandes en statut PENDING peuvent être annulées.");
        }

        for (OrderItem item : commande.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantite());
            productRepository.save(product);
        }

        commande.setStatut(OrderStatus.Canceles);

        Commande saved = commandeRepository.save(commande);

        return commandeMapper.toDto(saved);
    }

}

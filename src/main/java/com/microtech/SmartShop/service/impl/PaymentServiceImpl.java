package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.PaymentCreateDto;
import com.microtech.SmartShop.dto.PaymentDTO;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.Payment;
import com.microtech.SmartShop.entity.enums.OrderStatus;
import com.microtech.SmartShop.entity.enums.PaymentStatus;
import com.microtech.SmartShop.entity.enums.PaymentType;
import com.microtech.SmartShop.mapper.PaymentMapper;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.repository.PaymentRepository;
import com.microtech.SmartShop.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CommandeRepository commandeRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private static final BigDecimal LIMITE_ESPECES = new BigDecimal("20000");

    public PaymentDTO addPayment(Long commandeId, PaymentCreateDto dto) {

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (commande.getStatut() == OrderStatus.Canceles) {
            throw new RuntimeException("Impossible d'ajouter un paiement sur une commande annulée");
        }

        if (dto.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Montant invalide");
        }

        if (dto.getType() == PaymentType.Especes &&
                dto.getMontant().compareTo(LIMITE_ESPECES) > 0) {
            throw new RuntimeException("Paiement en espèces limité à 20 000 DH");
        }

        if (dto.getMontant().compareTo(commande.getMontantRestant()) > 0) {
            throw new RuntimeException("Montant dépasse le montant restant");
        }

        int numeroPaiement = commande.getPayments().size() + 1;

        Payment payment = Payment.builder()
                .numeroPaiement(numeroPaiement)
                .montant(dto.getMontant())
                .type(dto.getType())
                .datePaiement(LocalDate.now())
                .statut(PaymentStatus.Encaisse)
                .commande(commande)
                .reference(dto.getReference())
                .banque(dto.getBanque())
                .dateEcheance(dto.getDateEcheance())
                .build();

        paymentRepository.save(payment);

        BigDecimal nouveauMontantRestant = commande.getMontantRestant()
                .subtract(dto.getMontant());

        commande.setMontantRestant(nouveauMontantRestant);

        if (nouveauMontantRestant.compareTo(BigDecimal.ZERO) == 0) {
            commande.setStatut(OrderStatus.Confirmed);
        }

        commandeRepository.save(commande);

        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional
    public PaymentDTO validatePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        if (payment.getStatut() == PaymentStatus.Encaisse) {
            throw new RuntimeException("Paiement déjà validé");
        }

        if (payment.getCommande().getStatut() == OrderStatus.Canceles) {
            throw new RuntimeException("Impossible de valider un paiement sur une commande annulée");
        }
        payment.setStatut(PaymentStatus.Encaisse);
        paymentRepository.save(payment);

        return paymentMapper.toDto(payment);
    }


}

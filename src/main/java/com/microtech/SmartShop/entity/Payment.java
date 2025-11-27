package com.microtech.SmartShop.entity;

import com.microtech.SmartShop.entity.enums.PaymentStatus;
import com.microtech.SmartShop.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numeroPaiement;
    private double montant;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private LocalDate datePaiement;
    private LocalDate dateEncaissement;

    @Enumerated(EnumType.STRING)
    private PaymentStatus statut;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    private String reference;
    private String banque;
    private LocalDate dateEcheance;
}

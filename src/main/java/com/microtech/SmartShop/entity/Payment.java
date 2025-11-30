package com.microtech.SmartShop.entity;

import com.microtech.SmartShop.entity.enums.PaymentStatus;
import com.microtech.SmartShop.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroPaiement;

    @Column(nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;

    @Column(nullable = false)
    private LocalDate datePaiement;

    private LocalDate dateEncaissement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus statut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_id",nullable = false)
    private Commande commande;

    private String reference;
    private String banque;
    private LocalDate dateEcheance;
}

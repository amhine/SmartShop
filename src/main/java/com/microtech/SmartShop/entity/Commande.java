package com.microtech.SmartShop.entity;

import com.microtech.SmartShop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal sousTotalHT = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal montantRemise;

    @Column(nullable = false)
    private BigDecimal montantHTApresRemise;

    @Column(nullable = false)
    private BigDecimal montantTVA;

    @Column(nullable = false)
    private BigDecimal totalTTC;

    @Column(nullable = false)
    private BigDecimal montantRestant;


    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String codePromo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus statut = OrderStatus.Pending;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();


}

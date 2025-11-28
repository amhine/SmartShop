package com.microtech.SmartShop.entity;

import com.microtech.SmartShop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreation = LocalDateTime.now();

    private double sousTotalHT;
    private double montantRemise;
    private double montantHTApresRemise;
    private double montantTVA;
    private double totalTTC;
    private double montantRestant;

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String codePromo;

    @Enumerated(EnumType.STRING)
    private OrderStatus statut = OrderStatus.Pending;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    public boolean isFullyPaid() {
        return montantRestant <= 0.0;
    }
}

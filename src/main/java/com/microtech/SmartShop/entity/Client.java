package com.microtech.SmartShop.entity;

import com.microtech.SmartShop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "id")
public class Client extends User {

    @Column(nullable = false)
    private String nom;


    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CustomerTier customer;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commande> commandes;

    @Column(nullable = false)
    private Integer totalOrders;

    @Column(nullable = false)
    private BigDecimal totalSpent;

    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;

    @Column(nullable = false)
    private Boolean deleted;
    public Client() {
        this.deleted = false;
    }

}

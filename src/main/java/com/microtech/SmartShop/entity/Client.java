package com.microtech.SmartShop.entity;


import com.microtech.SmartShop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String nom;
    @Enumerated(EnumType.STRING)
    private CustomerTier customer;

//    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Commande> commandes;
}

package com.microtech.SmartShop.entity;


import com.microtech.SmartShop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {


    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier customer;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Commande> commandes;

    private boolean deleted = false;

    public LocalDateTime getFirstOrderDate() {
        return commandes != null && !commandes.isEmpty() ?
                commandes.stream()
                        .map(Commande::getDateCreation)
                        .min(LocalDateTime::compareTo)
                        .orElse(null)
                : null;
    }

    public LocalDateTime getLastOrderDate() {
        return commandes != null && !commandes.isEmpty() ?
                commandes.stream()
                        .map(Commande::getDateCreation)
                        .max(LocalDateTime::compareTo)
                        .orElse(null)
                : null;
    }

    public Integer getTotalOrders() {
        return commandes != null ? commandes.size() : 0;
    }

    public Double getTotalSpent() {
        return commandes != null ? commandes.stream()
                .mapToDouble(Commande::getTotalTTC)
                .sum() : 0.0;
    }

}

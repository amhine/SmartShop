package com.microtech.SmartShop.entity;

import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "id")
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

    @Column(nullable = false)
    private Boolean deleted;


    public Client() {
        super();
        this.deleted = false;
    }

    public Client(String username, String password, Role role, String nom, String email, CustomerTier customer) {
        super.setUsername(username);
        super.setPassword(password);
        super.setRole(role);
        this.nom = nom;
        this.email = email;
        this.customer = customer;
        this.deleted = false;
    }

    public LocalDateTime getFirstOrderDate() {
        return (commandes != null && !commandes.isEmpty()) ?
                commandes.stream()
                        .map(Commande::getDateCreation)
                        .min(LocalDateTime::compareTo)
                        .orElse(null)
                : null;
    }

    public LocalDateTime getLastOrderDate() {
        return (commandes != null && !commandes.isEmpty()) ?
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

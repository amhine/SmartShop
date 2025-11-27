package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.enums.CustomerTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private CustomerTier niveauFidelite;
    private Integer totalOrders;
    private Double totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
}

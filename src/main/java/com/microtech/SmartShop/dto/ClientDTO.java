package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.enums.CustomerTier;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private CustomerTier customer;
    private Integer totalOrders;
    private BigDecimal totalSpent;
}

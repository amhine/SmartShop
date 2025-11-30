package com.microtech.SmartShop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String nom;
    private BigDecimal prixUnitaire;
    private Integer stock;
}

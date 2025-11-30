package com.microtech.SmartShop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateDto {
    @NotBlank
    private String nom;
    @NotNull
    @Min(0)
    private BigDecimal prix;
    @NotNull
    @Min(0)
    private Integer stock;
}

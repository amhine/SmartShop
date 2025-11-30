package com.microtech.SmartShop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemCreateDto {
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantite;
}

package com.microtech.SmartShop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CommandeCreateDto {
    @NotNull
    private Long clientId;
    @NotEmpty
    private List<OrderItemCreateDto> items;
    private String codePromo;
}

package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CommandeDTO {
    private Long id;
    private LocalDateTime dateCreation;
    private double totalTTC;
    private OrderStatus statut;
}

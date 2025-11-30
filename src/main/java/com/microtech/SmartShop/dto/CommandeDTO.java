package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeDTO {

    private Long id;
    private Long clientId;
    private LocalDateTime dateCreation;
    private OrderStatus statut;
    private BigDecimal sousTotalHT;
    private BigDecimal montantRemise;
    private BigDecimal montantHTApresRemise;
    private BigDecimal montantTVA;
    private BigDecimal totalTTC;
    private BigDecimal montantRestant;
    private List<OrderItemDTO> items;
    private List<PaymentDTO> payments;
}

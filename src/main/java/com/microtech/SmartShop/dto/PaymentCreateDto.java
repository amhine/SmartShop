package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentCreateDto {
    @NotNull
    private BigDecimal montant;
    @NotNull
    private PaymentType type;
    private String reference;
    private String banque;
    private LocalDate dateEcheance;
}

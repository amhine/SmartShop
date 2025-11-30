package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.PaymentDTO;
import com.microtech.SmartShop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "commande.id", target = "commandeId")
    PaymentDTO toDto(Payment payment);
}

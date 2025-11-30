package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.dto.OrderItemDTO;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "items", target = "items")
    CommandeDTO toDto(Commande order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.nom", target = "productName")
    OrderItemDTO toItemDto(OrderItem item);

}

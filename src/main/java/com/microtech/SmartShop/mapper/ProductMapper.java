package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.ProductCreateDto;
import com.microtech.SmartShop.dto.ProductDTO;
import com.microtech.SmartShop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "prixUnitaire", target = "prixUnitaire")    ProductDTO toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(source = "prix", target = "prixUnitaire")
    Product toEntity(ProductCreateDto dto);
}

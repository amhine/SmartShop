package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.ProductDTO;
import com.microtech.SmartShop.entity.Product;

public class ProductMapper {

    public static ProductDTO toDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .nom(product.getNom())
                .prixUnitaire(product.getPrixUnitaire())
                .stock(product.getStock())
                .build();
    }

    public static Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setNom(dto.getNom());
        product.setPrixUnitaire(dto.getPrixUnitaire());
        product.setStock(dto.getStock());
        return product;
    }

    public static void updateEntity(Product product, ProductDTO dto) {
        product.setNom(dto.getNom());
        product.setPrixUnitaire(dto.getPrixUnitaire());
        product.setStock(dto.getStock());
    }
}

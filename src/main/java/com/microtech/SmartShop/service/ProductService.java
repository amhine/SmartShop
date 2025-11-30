package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.ProductCreateDto;
import com.microtech.SmartShop.dto.ProductDTO;
import com.microtech.SmartShop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    ProductDTO createProduct(ProductCreateDto dto);
    ProductDTO updateProduct(Long id, ProductCreateDto dto);
    void deleteProduct(Long id);
    Product getProductEntity(Long id);
    Page<ProductDTO> getAllProducts(Pageable pageable);


}

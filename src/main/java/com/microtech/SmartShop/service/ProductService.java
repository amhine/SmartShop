package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.ProductDTO;
import org.springframework.data.domain.Page;


public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    Page<ProductDTO> getAllProducts(int page, int size, String search);
}

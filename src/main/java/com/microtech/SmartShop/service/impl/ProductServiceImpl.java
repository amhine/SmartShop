package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.ProductDTO;
import com.microtech.SmartShop.entity.Product;
import com.microtech.SmartShop.mapper.ProductMapper;
import com.microtech.SmartShop.repository.ProductRepository;
import com.microtech.SmartShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toEntity(productDTO);
        Product saved = productRepository.save(product);
        return ProductMapper.toDto(saved);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        ProductMapper.updateEntity(product, productDTO);
        Product updated = productRepository.save(product);
        return ProductMapper.toDto(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        if (product.isDeleted()) {
            throw new RuntimeException("Produit supprimé");
        }
        return ProductMapper.toDto(product);
    }

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByDeletedFalseAndNomContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findByDeletedFalse(pageable);
        }

        return productPage.map(ProductMapper::toDto);
    }

}

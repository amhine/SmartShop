package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.ProductCreateDto;
import com.microtech.SmartShop.dto.ProductDTO;
import com.microtech.SmartShop.entity.Product;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.AccessDeniedException;
import com.microtech.SmartShop.mapper.ProductMapper;
import com.microtech.SmartShop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDto dto, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Seul un ADMIN peut créer un produit");
        }
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductCreateDto dto, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Seul un ADMIN peut modifier un produit");
        }
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Seul un ADMIN peut supprimer un produit");
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        Product product = productService.getProductEntity(id);
        ProductDTO dto = productMapper.toDto(product);
        return ResponseEntity.ok(dto);
    }


    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

}

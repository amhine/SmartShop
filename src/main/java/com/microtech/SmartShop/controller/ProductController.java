package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.ProductDTO;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.AccessDeniedException;
import com.microtech.SmartShop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Seul un ADMIN peut créer un produit");
        }
        return productService.createProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id,
                                    @RequestBody ProductDTO productDTO,
                                    HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Seul un ADMIN peut modifier un produit");
        }
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Seul un ADMIN peut supprimer un produit");
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }
}

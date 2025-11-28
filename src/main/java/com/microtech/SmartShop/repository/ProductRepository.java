package com.microtech.SmartShop.repository;

import com.microtech.SmartShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}

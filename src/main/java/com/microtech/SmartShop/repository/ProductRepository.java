package com.microtech.SmartShop.repository;

import com.microtech.SmartShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByDeletedFalse(Pageable pageable);

}

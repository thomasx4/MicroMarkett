package com.gestion.micromarket.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestion.micromarket.entity.Products;
import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    boolean existsByBarcode(String barcode);

    Optional<Products> findByBarcode(String barcode);

    List<Products> findByNameContainingIgnoreCase(String name);

    List<Products> findByCategoryId(Long categoryId);
}
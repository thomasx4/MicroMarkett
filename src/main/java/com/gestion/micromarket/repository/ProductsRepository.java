package com.gestion.micromarket.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestion.micromarket.entity.Products;
import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    boolean existsByBarcode(String barcode);
    
    boolean existsByBarcodeAndActiveTrue(String barcode);

    Optional<Products> findByBarcode(String barcode);
    
    Optional<Products> findByBarcodeAndActiveTrue(String barcode);

    List<Products> findByNameContainingIgnoreCase(String name);
    
    List<Products> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    List<Products> findByCategoryId(Long categoryId);
    
    List<Products> findByCategoryIdAndActiveTrue(Long categoryId);
    
    List<Products> findAllByActiveTrue();
    
    Optional<Products> findByIdAndActiveTrue(Long id);
    
    Optional<Products> findById(Long id);
}
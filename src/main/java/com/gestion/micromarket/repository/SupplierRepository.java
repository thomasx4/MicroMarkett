package com.gestion.micromarket.repository;

import com.gestion.micromarket.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findByTaxId(String taxId);

}
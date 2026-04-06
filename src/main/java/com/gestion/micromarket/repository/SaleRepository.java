package com.gestion.micromarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.micromarket.entity.Sales;

@Repository
public interface SaleRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByEmployeeId(Long employeeId);
}

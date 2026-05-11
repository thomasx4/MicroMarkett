package com.gestion.micromarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.micromarket.entity.Sales;

@Repository
public interface SaleRepository extends JpaRepository<Sales, Long> {
    /**
     * Busca las ventas realizadas por un empleado específico
     *
     * @param employeeId El ID del empleado
     * @return Lista de ventas del empleado
     */
    List<Sales> findByEmployeeId(Long employeeId);
}

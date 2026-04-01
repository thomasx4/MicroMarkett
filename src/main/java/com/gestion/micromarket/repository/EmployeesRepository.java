package com.gestion.micromarket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.micromarket.entity.Employees;

@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long>{
    Optional<Employees> findByDocumentNumber(String documentNumber);

} 
package com.gestion.micromarket.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.enums.Role;

@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    Optional<Employees> findByDocumentNumber(String documentNumber);

    List<Employees> findByRole(Role role);

    List<Employees> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    List<Employees> findByActive(Boolean active);


}
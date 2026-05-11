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
    /**
     * Busca un empleado por su Numero de Documento
     * 
     * @param documentNumber
     * @return empleado encontrado, o vacío si no existe
     */
    Optional<Employees> findByDocumentNumber(String documentNumber);

    /**
     * Obtiene todos los empleados que tienen un rol específico
     * 
     * @param role
     * @return lista con los empleados encontrados o lista vacía si ninguno coincide
     */
    List<Employees> findByRole(Role role);

    /**
     * Obtiene los empleados contratados dentro de un rango de fechas (ambos extremos incluidos)
     * 
     * @param startDate
     * @param endDate
     * @return lista de empleados contratados en ese período, o lista vacía si ninguno coincide
     */
    List<Employees> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene los empleados filtrados por su estado de actividad
     * 
     * @param active
     * @return ista de empleados con ese estado, o lista vacía si ninguno coincide
     */
    List<Employees> findByActive(Boolean active);
}
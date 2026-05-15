package com.gestion.micromarket.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestion.micromarket.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employees")
public class Employees {

    /**
     * Identificador único del empleado, generado automáticamente por la BD
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Número de documento de identidad del empleado
     */
    @Column(name = "document_number")
    private String documentNumber;

    /**
     * Nombre completo del empleado
     */
    @Column(name = "name")
    private String name;

    /**
     * Rol del empleado dentro del sistema. Se persiste como texto en la BD
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "password")
    private String password;

    /**
     * Fecha en que el empleado fue contratado
     */
    @Column(name = "hire_date")
    private LocalDate hireDate;

    /**
     * Salario actual del empleado
     */
    @Column(name = "salary")
    private BigDecimal salary;

    /**
     * Estado del empleado, por defecto es true
     */
    @Column(name = "active")
    private Boolean active = true;

    /**
     * Fecha y hora en que el empleado fúe registrado
     * Este no se puede actualizar después de creado
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Ventas realizadas por un empleado
     * Se carga de forma diferida ({@code LAZY}) y cualquier operación en cascada
     * sobre el empleado se propaga a sus ventas asociadas ({@code CascadeType.ALL}).
     */
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Sales> sales = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
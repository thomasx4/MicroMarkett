package com.gestion.micromarket.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestion.micromarket.entity.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employees")
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_number",  nullable = false)
    private String documentNumber;

    @Column(name = "name",  nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",  nullable = false)
    private Role role;

    @Column(name = "hire_date",  nullable = false)
    private LocalDate hireDate;

    @Column(name = "salary",  nullable = false)
    private BigDecimal salary;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Sales> sales = new ArrayList<>();

}
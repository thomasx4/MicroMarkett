package com.gestion.micromarket.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sales")
public class Sales {
    /**
     * Identificador único de la venta
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Empleado que realizó la venta
     */
    @ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "employee_id")
    private Employees employee;

    /**
     * Fecha y hora en que se realizó la venta
     */
    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    /**
     * Subtotal de la venta (sin IVA)
     */
    @Column()
    private BigDecimal subtotal;

    /**
     * Valor del IVA calculado para la venta
     */
    @Column()
    private BigDecimal vat;

    /**
     * Total de la venta (subtotal + IVA)
     */
    @Column()
    private BigDecimal total;

    /**
     * Lista de productos detallados de esta venta
     */
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleDetail> saleDetails = new ArrayList<>();

    /**
     * Se ejecuta antes de guardar la venta para asignar la fecha actual
     */
    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
    }
}
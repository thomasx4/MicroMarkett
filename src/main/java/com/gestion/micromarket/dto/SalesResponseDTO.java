package com.gestion.micromarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gestion.micromarket.enums.Role;

import lombok.Data;

@Data
public class SalesResponseDTO {
    /**
     * Identificador de la venta
     */
    private Long id;
    
    /**
     * Fecha y hora de la venta
     */
    private LocalDateTime saleDate;
    
    /**
     * Subtotal de la venta (sin IVA)
     */
    private BigDecimal subtotal;
    
    /**
     * Valor del IVA
     */
    private BigDecimal vat;
    
    /**
     * Total de la venta (subtotal + IVA)
     */
    private BigDecimal total;
    
    /**
     * ID del empleado que atendió la venta
     */
    private Long employeeId;
    
    /**
     * Nombre del empleado
     */
    private String employeeName;
    
    /**
     * Rol del empleado
     */
    private Role employeeRole;
    
    /**
     * Estado del empleado (activo/inactivo)
     */
    private Boolean employeeActive;
    
    /**
     * Lista de productos detallados de la venta
     */
    private List<SaleDetailResponseDTO> saleDetails;
}
package com.gestion.micromarket.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SupplierResponseDTO {

    /**
     * Identificador único del proveedor
     */
    private Long id;

    /**
     * Nombre del proveedor
     */
    private String name;

    /**
     * Identificación fiscal del proveedor 
     */
    private String taxId;

    /**
     * Número de teléfono del proveedor
     */
    private String phone;

    /**
     * Dirección física del proveedor
     */
    private String address;

    /**
     * Correo electrónico del proveedor
     */
    private String email;

    /**
     * Fecha y hora de creación del proveedor
     */
    private LocalDateTime createdAt;

}
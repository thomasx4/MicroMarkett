package com.gestion.micromarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequestDTO {

    /**
     * Nombre del proveedor
     */
    @NotBlank(message = "El nombre es obligatorio")
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

}
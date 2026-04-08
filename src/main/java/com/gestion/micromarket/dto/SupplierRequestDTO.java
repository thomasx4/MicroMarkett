package com.gestion.micromarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String taxId;

    private String phone;
    private String address;
    private String email;

}
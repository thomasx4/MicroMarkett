package com.gestion.micromarket.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SupplierResponseDTO {

    private Long id;
    private String name;
    private String taxId;
    private String phone;
    private String address;
    private String email;
    private LocalDateTime createdAt;

}
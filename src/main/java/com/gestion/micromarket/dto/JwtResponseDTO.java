package com.gestion.micromarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String jwt;
    private String role;
    private String name;
}
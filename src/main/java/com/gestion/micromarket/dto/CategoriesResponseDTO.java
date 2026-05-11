package com.gestion.micromarket.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoriesResponseDTO {
    /**
     * Id de la categoria
     */
    private Long id;

    /**
     * Nombre de la categoria
     */
    private String name;

    /**
     * Descripcion de la categoria
     */
    private String description;

    /**
     * Fecha y hora de creacion de la categoria
     */
    private LocalDateTime createdAt;

    
}

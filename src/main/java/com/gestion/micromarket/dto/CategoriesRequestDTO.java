package com.gestion.micromarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriesRequestDTO {

    /**
     * Nombre de la categoria
     */
    @NotBlank(message = "El nombre de la categoria es obligatoria")
    private String name;

    /**
     * Descripcion de la categoria
     */
    @NotBlank(message = "La descripcion de la categoria es obligatoria")
    private String description;

}



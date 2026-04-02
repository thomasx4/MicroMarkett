package com.gestion.micromarket.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductsRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "El código de barras es obligatorio")
    @Size(max = 50, message = "El código de barras no puede tener más de 50 caracteres")
    private String barcode;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Long stock;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
    
}

package com.gestion.micromarket.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductsRequestDTO {

    /**
     * Nombre del producto.
     * No puede estar en blanco y tiene una longitud máxima de 50 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    /**
     * Código de barras único del producto.
     * No puede estar en blanco y tiene una longitud máxima de 50 caracteres.
     */
    @NotBlank(message = "El código de barras es obligatorio")
    @Size(max = 50, message = "El código de barras no puede tener más de 50 caracteres")
    private String barcode;

    /**
     * Precio del producto.
     * No puede ser nulo y debe ser un valor positivo mayor a 0.
     */
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    /**
     * Cantidad de unidades disponibles en inventario.
     * No puede ser nulo y no puede ser negativo (puede ser cero o positivo).
     */
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Long stock;

    /**
     * Estado del producto (activo/inactivo).
     * No puede ser nulo. false indica que el producto será marcado como eliminado lógicamente.
     */
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;

    /**
     * Identificador de la categoría a la que pertenece el producto.
     * No puede ser nulo. Debe corresponder a una categoría existente en la base de datos.
     */
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
    
}
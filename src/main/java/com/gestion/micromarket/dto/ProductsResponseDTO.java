package com.gestion.micromarket.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductsResponseDTO {

    /**
     * Identificador único del producto.
     */
    private Long id;

    /**
     * Nombre del producto.
     */
    private String name;

    /**
     * Código de barras único del producto.
     */
    private String barcode;

    /**
     * Precio del producto (valor monetario).
     */
    private BigDecimal price;

    /**
     * Cantidad de unidades disponibles en inventario.
     */
    private Long stock;

    /**
     * Estado del producto (activo/inactivo).
     * false indica que el producto fue eliminado lógicamente.
     */
    private Boolean active;

    /**
     * Fecha y hora de creación del producto.
     */
    private LocalDateTime createdAt;

    /**
     * Nombre de la categoría a la que pertenece el producto.
     * Este campo proviene de la relación con la entidad Categories.
     */
    private String categoryName;
    
}
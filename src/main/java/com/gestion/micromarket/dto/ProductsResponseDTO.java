package com.gestion.micromarket.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductsResponseDTO {

    private Long id;
    private String name;
    private String barcode;
    private BigDecimal price;
    private Long stock;
    private Boolean active;
    private LocalDateTime createdAt;
    private String categoryName;
    
}
package com.gestion.micromarket.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductsResponseDTO {

    private Long id;
    private String name;
    private String barcode;
    private Double price;
    private Long stock;
    private Boolean active;
    private LocalDateTime createdAt;
    private String categoryName;
    
}
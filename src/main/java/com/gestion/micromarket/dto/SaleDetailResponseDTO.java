package com.gestion.micromarket.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SaleDetailResponseDTO {
    private Long id;
    private Long saleId;
    private Long productId;
    private String productName;
    private String productBarcode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
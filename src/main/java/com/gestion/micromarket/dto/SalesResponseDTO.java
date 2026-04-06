package com.gestion.micromarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class SalesResponseDTO {
    private Long id;
    private LocalDateTime saleDate;
    private BigDecimal subtotal;
    private BigDecimal vat;
    private BigDecimal total;


    private EmployeesResponseDTO employee;
    private List<SaleDetailResponseDTO> saleDetails;
}

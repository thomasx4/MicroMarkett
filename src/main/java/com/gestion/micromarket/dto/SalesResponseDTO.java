package com.gestion.micromarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gestion.micromarket.enums.Role;

import lombok.Data;

@Data
public class SalesResponseDTO {
    private Long id;
    private LocalDateTime saleDate;
    private BigDecimal subtotal;
    private BigDecimal vat;
    private BigDecimal total;
    private Long employeeId;
    private String employeeName;
    private Role employeeRole;
    private Boolean employeeActive;
    private List<SaleDetailResponseDTO> saleDetails;
}
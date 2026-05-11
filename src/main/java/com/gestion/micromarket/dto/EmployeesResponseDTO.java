package com.gestion.micromarket.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EmployeesResponseDTO {

    private Long id;

    private String name;

    private String documentNumber;

    private String role;

    private LocalDate hireDate;

    private BigDecimal salary;

    private Boolean active;

    private LocalDateTime createdAt;
}

package com.gestion.micromarket.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String documentNumber;
    private String name;
    private String password;
    private String role;
    private LocalDate hireDate;
    private BigDecimal salary;
}

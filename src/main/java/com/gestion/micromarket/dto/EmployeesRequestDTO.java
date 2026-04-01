package com.gestion.micromarket.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.gestion.micromarket.entity.enums.Role;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeesRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotNull(message = "El numero de documento es obligatorio")
    @Size(min = 5, max = 10, message = "El numero de documento debe tener entre 5 y 10 caracteres")
    private String documentNumber;
    @NotNull(message = "El rol es obligatorio")
    private Role role;
    @NotNull(message = "El fecha de contratacion es obligatorio")
    @PastOrPresent(message = "La fecha de contratacion no puede ser futura")
    private LocalDate hireDate;
    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario no puede ser menor a 0.0")
    @Positive(message = "El salario debe de ser mayor a 0")
    private BigDecimal salary;

}
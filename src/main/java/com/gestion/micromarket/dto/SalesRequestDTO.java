package com.gestion.micromarket.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SalesRequestDTO {
    @NotNull(message = "El ID del empleado es obligatorio")
    private Long employeeId;

    @NotNull(message = "Los detalles de venta son obligatorios")
    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Size(min = 1, message = "La venta debe tener al menos un producto")
    private List<SaleDetailRequestDTO> saleDetails;
}

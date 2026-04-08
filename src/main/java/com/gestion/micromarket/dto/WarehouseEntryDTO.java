package com.gestion.micromarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseEntryDTO {

    @NotNull
    private Long productId;

    @NotNull
    private Long supplierId;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int quantity;
}
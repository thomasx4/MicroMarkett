package com.gestion.micromarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDTO {
    private Long id;
    private String name;
    private String documentNumber;
    private String role;
    private Boolean active;
}
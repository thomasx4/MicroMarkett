package com.gestion.micromarket.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String documentNumber;
    private String password;
}
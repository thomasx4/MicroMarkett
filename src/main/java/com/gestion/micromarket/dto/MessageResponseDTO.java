package com.gestion.micromarket.dto;

import lombok.Data;

@Data
public class MessageResponseDTO {
    private String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }
}
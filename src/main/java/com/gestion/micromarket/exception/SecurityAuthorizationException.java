package com.gestion.micromarket.exception;

public class SecurityAuthorizationException extends RuntimeException {

    /**
     * Construye una nueva excepción de autorización con un mensaje detallado.
     * 
     * @param message El mensaje que explica la razón del fallo de seguridad
     */
    public SecurityAuthorizationException(String message) {
        super(message);
    }
}
package com.gestion.micromarket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.micromarket.dto.JwtResponseDTO;
import com.gestion.micromarket.dto.LoginRequestDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.RegisterRequestDTO;
import com.gestion.micromarket.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Obtiene mensaje de registro confirmado
     * 
     * @param request
     * @return MessageResponseDTO de registro exitoso (201) o si algo falla (400)
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        try {
            MessageResponseDTO response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Autentica un usuario y devuelve un token JWT si las credenciales son válidas
     * 
     * @param request 
     * @return JwtResponseDTO token (200) si las credenciales son válidas (401) si no lo son
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            JwtResponseDTO response = authService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Genera un token JWT nuevo a partir de uno válido que aún no ha expirado
     * 
     * @param request solicitud HTTP en la que se extrae el header 
     * @return nuevo token JWT con estado 200, o estado 401 si el header está ausente, mal formado o el token no es válido   
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.replaceFirst("Bearer ", "");

        try {
            JwtResponseDTO response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
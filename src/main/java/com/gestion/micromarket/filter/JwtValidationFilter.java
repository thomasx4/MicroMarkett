package com.gestion.micromarket.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gestion.micromarket.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import tools.jackson.databind.ObjectMapper;


@Component
@RequiredArgsConstructor
@Log4j2
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Valida el token JWT presente en el header de la solicitud.
     * Si el token es válido, extrae el email, id y rol del empleado y los adjunta como atributos de la solicitud para que estén disponibles en los controladores
     * Si el token es inválido, ausente o mal formado, responde con estado 401 y detiene la cadena de filtros
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException si ocurre un error en el procesamiento del filtro
     * @throws IOException si ocurre un error al escribir la respuesta de error
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Validar que exista y tenga formato Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(
                    Map.of("error", "Header Authorization es requerido con formato Bearer <token>")));
            return;
        }

        String token = authHeader.replaceFirst("Bearer ", "");

        try {
            if (jwtService.isTokenValid(token)) {
                // Extraer información del token
                String email = jwtService.extractEmail(token);
                Long employeeId = jwtService.extractEmployeeId(token);
                String role = jwtService.extractRole(token);

                // Setear atributos para usar en los controllers si es necesario
                request.setAttribute("email", email);
                request.setAttribute("employeeId", employeeId);
                request.setAttribute("role", role);

                // Continuar con la petición
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(
                        Map.of("error", "Token inválido o expirado")));
            }
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(
                    Map.of("error", "Error validando el token: " + e.getMessage())));
        }
    }

    /**
     * Define las rutas públicas que no requieren validación de token JWT.
     * Cualquier solicitud cuya URI comience con /auth/login ,
     * /auth/register o /auth/refresh omite este filtro.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        return path.startsWith(contextPath + "/auth/login") ||
                path.startsWith(contextPath + "/auth/register") ||
                path.startsWith(contextPath + "/auth/refresh");
    }

    /**
     * Clase auxiliar para construir mapas de un solo par clave-valor en las respuestas de error.
     * Evita el uso directo de java.util.Map#of por compatibilidad con el serializador.
     */
    private static class Map {
        static java.util.Map<String, String> of(String key, String value) {
            return java.util.Collections.singletonMap(key, value);
        }
    }
}
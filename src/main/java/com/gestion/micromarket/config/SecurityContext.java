package com.gestion.micromarket.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class SecurityContext {

    /**
     * accede a los atributos de la petición HTTP almacenados por el filtro de
     * validación JWT
     * 
     * @return El nombre del rol almacenado en el token o null si no existe un rol
     *         asociado a la petición actual.
     */
    public String getCurrentRole() {
        Object role = RequestContextHolder.currentRequestAttributes()
                .getAttribute("role", RequestAttributes.SCOPE_REQUEST);
                
        if (role != null) {
            return role.toString();
        } else {
            return null;
        }
    }
}
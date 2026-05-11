package com.gestion.micromarket.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.gestion.micromarket.filter.JwtValidationFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValidationFilter) {
        
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
        
        // Setear el filtro
        registrationBean.setFilter(jwtValidationFilter);
        
        // Aplicar a todas las rutas
        registrationBean.addUrlPatterns("/*");
        
        // Alta prioridad para que se ejecute antes que otros filtros
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        
        return registrationBean;
    }
}
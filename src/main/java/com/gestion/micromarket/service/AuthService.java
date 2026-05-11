package com.gestion.micromarket.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gestion.micromarket.dto.JwtResponseDTO;
import com.gestion.micromarket.dto.LoginRequestDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.RegisterRequestDTO;
import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.enums.Role;
import com.gestion.micromarket.repository.EmployeesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final EmployeesRepository employeesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registra un nuevo empleado, solo admins puede hacerlo desde el backend
     * 
     * @param request recibe datos del empleado a registrar, documento, rol y contrseña
     * @return mensaje de confirmación de registro
     * @throws RuntimeException si el número de docuemnto ingresado ya está registrado, si el rol es inválido
     */
    public MessageResponseDTO register(RegisterRequestDTO request) {
       
        if (employeesRepository.findByDocumentNumber(request.getDocumentNumber()).isPresent()) {
            throw new RuntimeException("El número de documento ya está registrado: " + request.getDocumentNumber());
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido. Debe ser: administrator, cashier o assistant");
        }

        Employees employee = new Employees();
        employee.setDocumentNumber(request.getDocumentNumber());
        employee.setName(request.getName());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(role);
        employee.setHireDate(request.getHireDate());
        employee.setSalary(request.getSalary());
        employee.setActive(true);
        employee.setCreatedAt(LocalDateTime.now());

        employeesRepository.save(employee);

        return new MessageResponseDTO("Empleado registrado exitosamente");
    }

    /**
     * Autentica un empleado verificando su número de documento, contraseña y estado activo
     * Si las credenciales son válidas genera y devuelve un token JWT
     * 
     * @param request
     * @return token JWT junto con el rol y nombre del empleado autenticado
     * @throws RuntimeException si el numero de documento ingresado aún no esta registrado, la contraseña es incorrecta o el empleado está inactiv
     */
    public JwtResponseDTO login(LoginRequestDTO request) {
        
        Optional<Employees> employeeOpt = employeesRepository.findByDocumentNumber(request.getDocumentNumber());
        
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Número de documento no registrado: " + request.getDocumentNumber());
        }

        Employees employee = employeeOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!employee.getActive()) {
            throw new RuntimeException("El empleado no está activo. Contacte al administrador");
        }

        // Generar token JWT
        String jwt = jwtService.generateToken(
            employee.getId(),
            employee.getRole().name(),
            employee.getDocumentNumber()
        );

        return new JwtResponseDTO(jwt, employee.getRole().name(), employee.getName());
    }

    /**
     * Genera un nuevo token a partir de uno válido
     * 
     * @param token 
     * @return nuevo token JWT 
     * @throws RuntimeException si el empleado no ha sido encontrado
     * @throws Exception si el token es inválido
     */
    public JwtResponseDTO refreshToken(String token) throws Exception {
        String newToken = jwtService.refreshToken(token);
        String role = jwtService.extractRole(token);
        String email = jwtService.extractEmail(token);
        
        Optional<Employees> employeeOpt = employeesRepository.findByDocumentNumber(email);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Empleado no encontrado");
        }
        
        return new JwtResponseDTO(newToken, role, employeeOpt.get().getName());
    }
}
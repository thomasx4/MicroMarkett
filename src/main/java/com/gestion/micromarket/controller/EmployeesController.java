package com.gestion.micromarket.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.micromarket.dto.EmployeesRequestDTO;
import com.gestion.micromarket.dto.EmployeesResponseDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.enums.Role;
import com.gestion.micromarket.service.EmployeesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeesController {

    /** Servicio de usuarios */
    private final EmployeesService employeesService;

    /**
     * Registra un nuevo empleado
     * 
     * @param employeesRequestDTO datos para crear un empleado
     * @return MessageResponseDTO Mensaje de Confirmación (201)
     */
    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createEmplyees(
            @Valid @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        MessageResponseDTO messageResponseDTO = employeesService.createrEmployees(employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDTO);
    }

    /**
     * Obtiene la lista de todos los empleados 
     * 
     * @return List<EmployeesResponseDTO> Lista con todos los empleados (201) o si algo falla (400)
     */
    @GetMapping()
    public ResponseEntity<List<EmployeesResponseDTO>> getAllEmployees() {
        try {
            List<EmployeesResponseDTO> response = employeesService.getAllEmployees();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Busca un empleado por su id 
     * 
     * @param id 
     * @return Optional<EmployeesResponseDTO> Empleado encontrado (201)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<EmployeesResponseDTO>> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeesResponseDTO> response = employeesService.getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Busca un empleado por su número de documento
     * 
     * @param documentNumber
     * @return EmployeesResponseDTO Empleado encontrado (201)
     */
    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<EmployeesResponseDTO> getEmployeeByDocumentNumber(@PathVariable String documentNumber) {
        EmployeesResponseDTO response = employeesService.getEmployeeByDocumentNumber(documentNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Obtiene los empleados que tienen un rol específico
     * 
     * @param role puede ser: administrator, cashier o assistant
     * @return List<EmployeesResponseDTO> lista de empleados con el rol específico (201)
     * @throws RuntimeException si el valor de (rol) no es válido
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByRole(@PathVariable String role) {

        if (!role.equalsIgnoreCase("administrator") && !role.equalsIgnoreCase("cashier")
                && !role.equalsIgnoreCase("assistant")) {
            throw new RuntimeException("El rol debe de ser 'administrator', 'cashier', o 'assistant' no: " + role);
        }

        Role rolEnum = Role.valueOf(role);

        List<EmployeesResponseDTO> response = employeesService.getEmployeesByRole(rolEnum);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Obtiene los empleados filtrados por su estado de actividad
     * 
     * @param active puede ser activo=true o inactivo=false
     * @return List<EmployeesResponseDTO> lista de empleados con el estado específico (201)
     * @throws RuntimeException si el valor de (active) no es ni true ni false
     */
    @GetMapping("active/{active}")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByActive(@PathVariable String active) {

        if (!active.equalsIgnoreCase("true") && !active.equalsIgnoreCase("false")) {
            throw new RuntimeException("El valor debe ser 'true' o 'false', no: " + active);
        }

        Boolean activeBool = Boolean.valueOf(active);
        List<EmployeesResponseDTO> response = employeesService.getEmployyesByActive(activeBool);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Obtiene los empleados contratados dentro de un rango de fecha específico
     * 
     * @param startDate 
     * @param endDate
     * @return List<EmployeesResponseDTO> lista de empleados contratados en el período específico (200)
     */
    @GetMapping("/hire-date-range")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByHireDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<EmployeesResponseDTO> response = employeesService.getEmployeesByHireDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza compleatamente los datos de un emepleado existente
     * 
     * @param id
     * @param employeesRequestDTO
     * @return EmployeesResponseDTO empleado con datos actualizados (200)
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeesResponseDTO> updateEmployee(@PathVariable Long id,
            @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        EmployeesResponseDTO response = employeesService.updateEmployeeComplete(id, employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Actualiza uno o más campos específicos de un empleado existente sin reemplazar el registro completo
     * 
     * @param id
     * @param employeesRequestDTO
     * @return EmployeesResponseDTO empleado con dato o datos actualizados
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeesResponseDTO> updateSpecificEmployee(@PathVariable Long id,
            @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        EmployeesResponseDTO response = employeesService.updateEmployee(id, employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Elimina un empleado existente con el id
     * 
     * @param id
     * @return MessageResponseDTO mensaje de confirmación de eliminacion (200)
     */
    @DeleteMapping("/{id}")

    public ResponseEntity<MessageResponseDTO> deleteEmployeeByid(@PathVariable Long id) {
        MessageResponseDTO messageResponseDTO = employeesService.deleteEmployeeByid(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);

    }

    /**
     * Elimina un empleado por el número de documento
     * 
     * @param documentNumber
     * @return MessageResponseDTO Mensaje de confirmación de eliminacion (200)
     */
    @DeleteMapping("/documentNumber/{documentNumber}")

    public ResponseEntity<MessageResponseDTO> deleteEmployeeByDocumentNumber(@PathVariable String documentNumber) {
        MessageResponseDTO messageResponseDTO = employeesService.deleteEmployeeByNumberDocument(documentNumber);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);

    }

}

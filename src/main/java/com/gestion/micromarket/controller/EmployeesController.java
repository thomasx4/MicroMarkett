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
import com.gestion.micromarket.entity.enums.Role;
import com.gestion.micromarket.service.EmployeesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeesController {

    /** Servicio de usuarios */
    private final EmployeesService employeesService;

    // ------------------------------- CREATE -------------------------------
    /**
     * 
     * @param employeesRequestDTO datos para crear un usuario (este tipo de comentario es paera codgio sencillo cunado no es complejo)
     * @return MessageResponseDTO
     */
    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createEmplyees(
            @Valid @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        MessageResponseDTO messageResponseDTO = employeesService.createrEmployees(employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDTO);
    }

    // ------------------------------- GET ALL ----------------------------------
    /** Este codigo es para obtener todos los empleados (este tipo de comentario es cuando el codigo es coplejo y debe de ser explicado detalladamente) */
    @GetMapping()
    public ResponseEntity<List<EmployeesResponseDTO>> getAllEmployees() {
        try {
            List<EmployeesResponseDTO> response = employeesService.getAllEmployees();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // ------------------------------- GET BY ID
    // -------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<Optional<EmployeesResponseDTO>> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeesResponseDTO> response = employeesService.getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------- GET BY DOCUMENT NUMBER
    // ---------------------------------------

    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<EmployeesResponseDTO> getEmployeeByDocumentNumber(@PathVariable String documentNumber) {
        EmployeesResponseDTO response = employeesService.getEmployeeByDocumentNumber(documentNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------ GET BY ROLE
    // ------------------------------------------------

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

    // ------------------------------ GET BY ACTIVE
    // ------------------------------------------------

    @GetMapping("active/{active}")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByActive(@PathVariable String active) {

        if (!active.equalsIgnoreCase("true") && !active.equalsIgnoreCase("false")) {
            throw new RuntimeException("El valor debe ser 'true' o 'false', no: " + active);
        }

        Boolean activeBool = Boolean.valueOf(active);
        List<EmployeesResponseDTO> response = employeesService.getEmployyesByActive(activeBool);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------ GET BY HIRE DATE RANGE
    // -----------------------------------------------

    @GetMapping("/hire-date-range")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByHireDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<EmployeesResponseDTO> response = employeesService.getEmployeesByHireDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    // ------------------------------ UPDATE COMPLETE
    // -----------------------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<EmployeesResponseDTO> updateEmployee(@PathVariable Long id,
            @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        EmployeesResponseDTO response = employeesService.updateEmployeeComplete(id, employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------ UPDATE ONLY ONE O MORE
    // -----------------------------------------------

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeesResponseDTO> updateSpecificEmployee(@PathVariable Long id,
            @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        EmployeesResponseDTO response = employeesService.updateEmployee(id, employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------ DELETE BY ID
    // -----------------------------------------------

    @DeleteMapping("/{id}")

    public ResponseEntity<MessageResponseDTO> deleteEmployeeByid(@PathVariable Long id) {
        MessageResponseDTO messageResponseDTO = employeesService.deleteEmployeeByid(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);

    }

    // ------------------------------ DELETE BY DOCUMENT NUMBER
    // -----------------------------------------------

    @DeleteMapping("/documentNumber/{documentNumber}")

    public ResponseEntity<MessageResponseDTO> deleteEmployeeByDocumentNumber(@PathVariable String documentNumber) {
        MessageResponseDTO messageResponseDTO = employeesService.deleteEmployeeByNumberDocument(documentNumber);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);

    }

}

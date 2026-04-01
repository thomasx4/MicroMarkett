package com.gestion.micromarket.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final EmployeesService employeesService;

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createEmplyees(
            @Valid @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        MessageResponseDTO messageResponseDTO = employeesService.createrEmployees(employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<EmployeesResponseDTO>> getAllEmployees() {
        try {
            List<EmployeesResponseDTO> response = employeesService.getAllEmployees();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<EmployeesResponseDTO>> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeesResponseDTO> response = employeesService.getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<EmployeesResponseDTO> getEmployeeByDocumentNumber(@PathVariable String documentNumber) {
        EmployeesResponseDTO response = employeesService.getEmployeeByDocumentNumber(documentNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByRole(@PathVariable Role role) {
        List<EmployeesResponseDTO> response = employeesService.getEmployeesByRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/hire-date-range")
    public ResponseEntity<List<EmployeesResponseDTO>> getEmployeesByHireDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<EmployeesResponseDTO> response = employeesService.getEmployeesByHireDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeesResponseDTO> updateEmployee(@PathVariable Long id,
            @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        EmployeesResponseDTO response = employeesService.updateEmployeeComplete(id, employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeesResponseDTO> updateSpecificEmployee(@PathVariable Long id,
            @RequestBody EmployeesRequestDTO employeesRequestDTO) {
        EmployeesResponseDTO response = employeesService.updateEmployee(id, employeesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

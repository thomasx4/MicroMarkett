package com.gestion.micromarket.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.SalesRequestDTO;
import com.gestion.micromarket.dto.SalesResponseDTO;
import com.gestion.micromarket.service.SaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    // ------------------------------- CREATE -------------------------------
    @PostMapping
    public ResponseEntity<MessageResponseDTO> createSale(@Valid @RequestBody SalesRequestDTO salesRequestDTO) {
        MessageResponseDTO response = saleService.createSale(salesRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ------------------------------- GET ALL -------------------------------
    @GetMapping
    public ResponseEntity<List<SalesResponseDTO>> getAllSales() {
        List<SalesResponseDTO> response = saleService.getAllSales();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- GET BY ID -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<SalesResponseDTO>> getSaleById(@PathVariable Long id) {
        Optional<SalesResponseDTO> response = saleService.getSaleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- GET BY EMPLOYEE ID
    // -------------------------------
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<SalesResponseDTO>> getSalesByEmployeeId(@PathVariable Long employeeId) {
        List<SalesResponseDTO> response = saleService.getSalesByEmployeeId(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- UPDATE -------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> updateSale(@PathVariable Long id,
            @Valid @RequestBody SalesRequestDTO salesRequestDTO) {
        SalesResponseDTO response = saleService.updateSale(id, salesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- DELETE -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteSaleById(@PathVariable Long id) {
        MessageResponseDTO response = saleService.deleteSaleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
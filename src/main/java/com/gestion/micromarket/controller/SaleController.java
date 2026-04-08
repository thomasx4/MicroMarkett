package com.gestion.micromarket.controller;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> getSaleById(@PathVariable Long id) {
        SalesResponseDTO salesResponseDTO = saleService.getSaleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(salesResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<SalesResponseDTO>> getAllSales() {
        List<SalesResponseDTO> salesResponseDTOs = saleService.getAllSales();
        return ResponseEntity.ok(salesResponseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> updateSale(@PathVariable Long id,
            @Valid @RequestBody SalesRequestDTO salesRequestDTO) {
        SalesResponseDTO salesResponseDTO = saleService.updateSale(id, salesRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(salesResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteSale(@PathVariable Long id) {
        MessageResponseDTO response = saleService.deleteSale(id);
        return ResponseEntity.ok(response);
    }

}
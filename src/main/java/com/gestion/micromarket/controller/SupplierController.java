package com.gestion.micromarket.controller;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.service.SupplierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<MessageResponseDTO> create(@Valid @RequestBody SupplierRequestDTO supplierRequestDTO) {
        MessageResponseDTO response = supplierService.create(supplierRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAll() {
        List<SupplierResponseDTO> response = supplierService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getById(@PathVariable Long id) {
        SupplierResponseDTO response = supplierService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO supplierRequestDTO) {
        MessageResponseDTO response = supplierService.update(id, supplierRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable Long id) {
        MessageResponseDTO response = supplierService.delete(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/warehouse-entry")
    public ResponseEntity<MessageResponseDTO> warehouseEntry(@Valid @RequestBody WarehouseEntryDTO warehouseEntryDTO) {
        MessageResponseDTO response = supplierService.warehouseEntry(warehouseEntryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
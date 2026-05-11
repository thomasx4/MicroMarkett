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

    /**
     * Crea una nueva venta
     *
     * @param salesRequestDTO Datos de la venta
     * @return Respuesta con mensaje de éxito o error
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO> createSale(@Valid @RequestBody SalesRequestDTO salesRequestDTO) {
        try {
            MessageResponseDTO response = saleService.createSale(salesRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Busca una venta por su ID
     *
     * @param id ID de la venta
     * @return Datos de la venta encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> getSaleById(@PathVariable Long id) {
        try {
            SalesResponseDTO salesResponseDTO = saleService.getSaleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(salesResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtiene todas las ventas registradas
     *
     * @return Lista de todas las ventas
     */
    @GetMapping
    public ResponseEntity<List<SalesResponseDTO>> getAllSales() {
        try {
            List<SalesResponseDTO> salesResponseDTOs = saleService.getAllSales();
            return ResponseEntity.ok(salesResponseDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Actualiza los datos de una venta existente
     *
     * @param id ID de la venta a actualizar
     * @param salesRequestDTO Nuevos datos de la venta
     * @return Datos actualizados de la venta
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalesResponseDTO> updateSale(@PathVariable Long id,
            @Valid @RequestBody SalesRequestDTO salesRequestDTO) {
        try {
            SalesResponseDTO salesResponseDTO = saleService.updateSale(id, salesRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(salesResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Elimina una venta por su ID
     *
     * @param id ID de la venta a eliminar
     * @return Mensaje confirmando la eliminación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteSale(@PathVariable Long id) {
        try {
            MessageResponseDTO response = saleService.deleteSale(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

}
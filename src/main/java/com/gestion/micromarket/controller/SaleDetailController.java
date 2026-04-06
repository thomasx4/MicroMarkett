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
import com.gestion.micromarket.dto.SaleDetailRequestDTO;
import com.gestion.micromarket.dto.SaleDetailResponseDTO;
import com.gestion.micromarket.service.SaleDetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sale-details")
public class SaleDetailController {

    private final SaleDetailService saleDetailService;

    // ------------------------------- CREATE -------------------------------
    @PostMapping
    public ResponseEntity<MessageResponseDTO> createSaleDetail(
            @Valid @RequestBody SaleDetailRequestDTO saleDetailRequestDTO) {
        MessageResponseDTO response = saleDetailService.createSaleDetail(saleDetailRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ------------------------------- GET ALL -------------------------------
    @GetMapping
    public ResponseEntity<List<SaleDetailResponseDTO>> getAllSaleDetails() {
        List<SaleDetailResponseDTO> response = saleDetailService.getAllSaleDetails();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- GET BY ID -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<SaleDetailResponseDTO>> getSaleDetailById(@PathVariable Long id) {
        Optional<SaleDetailResponseDTO> response = saleDetailService.getSaleDetailById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- GET BY SALE ID
    // -------------------------------
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<SaleDetailResponseDTO>> getSaleDetailsBySaleId(@PathVariable Long saleId) {
        List<SaleDetailResponseDTO> response = saleDetailService.getSaleDetailsBySaleId(saleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- GET BY PRODUCT ID
    // -------------------------------
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<SaleDetailResponseDTO>> getSaleDetailsByProductId(@PathVariable Long productId) {
        List<SaleDetailResponseDTO> response = saleDetailService.getSaleDetailsByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- UPDATE -------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<SaleDetailResponseDTO> updateSaleDetail(@PathVariable Long id,
            @Valid @RequestBody SaleDetailRequestDTO saleDetailRequestDTO) {
        SaleDetailResponseDTO response = saleDetailService.updateSaleDetail(id, saleDetailRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- DELETE BY ID -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteSaleDetailById(@PathVariable Long id) {
        MessageResponseDTO response = saleDetailService.deleteSaleDetailById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ------------------------------- DELETE ALL BY SALE ID
    // -------------------------------
    @DeleteMapping("/sale/{saleId}")
    public ResponseEntity<MessageResponseDTO> deleteSaleDetailsBySaleId(@PathVariable Long saleId) {
        MessageResponseDTO response = saleDetailService.deleteSaleDetailsBySaleId(saleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
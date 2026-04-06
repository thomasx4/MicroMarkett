package com.gestion.micromarket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.service.ProductsService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;

    @PostMapping
    public MessageResponseDTO create(@Valid @RequestBody ProductsRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ProductsResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductsResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/name/{name}")
    public List<ProductsResponseDTO> findByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @GetMapping("/barcode/{barcode}")
    public ProductsResponseDTO findByBarcode(@PathVariable String barcode) {
        return service.findByBarcode(barcode);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductsResponseDTO> findByCategory(@PathVariable Long categoryId) {
        return service.findByCategory(categoryId);
    }

    @PutMapping("/{id}")
    public MessageResponseDTO update(@PathVariable Long id,
                                    @Valid @RequestBody ProductsRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
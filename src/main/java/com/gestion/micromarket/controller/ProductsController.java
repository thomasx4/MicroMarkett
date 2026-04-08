package com.gestion.micromarket.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Supplier;
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
    public MessageResponseDTO softDelete(@PathVariable Long id) {
        return service.softDelete(id);
    }

    @PatchMapping("/{id}/restore")
    public MessageResponseDTO restore(@PathVariable Long id) {
        return service.restore(id);
    }

    // Endpoints para gestionar proveedores (ManyToMany)
    @PostMapping("/{productId}/suppliers/{supplierId}")
    public MessageResponseDTO addSupplier(@PathVariable Long productId, 
                                        @PathVariable Long supplierId) {
        return service.addSupplier(productId, supplierId);
    }

    @DeleteMapping("/{productId}/suppliers/{supplierId}")
    public MessageResponseDTO removeSupplier(@PathVariable Long productId, 
                                            @PathVariable Long supplierId) {
        return service.removeSupplier(productId, supplierId);
    }

    @GetMapping("/{productId}/suppliers")
    public Set<Supplier> getSuppliersByProduct(@PathVariable Long productId) {
        return service.getSuppliersByProduct(productId);
    }
}
package com.gestion.micromarket.controller;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.service.SupplierService;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAll() {
        try {
            List<SupplierResponseDTO> list = supplierService.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getById(@PathVariable Long id) {
        try {
            SupplierResponseDTO response = supplierService.getById(id);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> create(@Valid @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            MessageResponseDTO messageResponseDTO = supplierService.create(supplierRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al crear el proveedor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            MessageResponseDTO messageResponseDTO = supplierService.update(id, supplierRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al actualizar el proveedor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable Long id) {
        try {
            MessageResponseDTO messageResponseDTO = supplierService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al eliminar el proveedor"));
        }
    }

    // RELACION DE PROVEEDOR Y PRODUCTO

    // AGREGAR PRODUCTO A PROVEEDOR

    @PostMapping("/{supplierId}/products/{productId}")
    public MessageResponseDTO addProduct(@PathVariable Long supplierId,@PathVariable Long productId) {

        return supplierService.addProduct(supplierId, productId);
    }

    // ELIMINAR PRODUCTO

    @DeleteMapping("/{supplierId}/products/{productId}")
    public MessageResponseDTO removeProduct(@PathVariable Long supplierId, @PathVariable Long productId) {

        return supplierService.removeProduct(supplierId, productId);
    }

    // OBTENER PRODUCTOS DEL PROVEEDOR

    @GetMapping("/{supplierId}/products")
    public Set<Products> getProductsBySupplier(@PathVariable Long supplierId) {

        return supplierService.getProductsBySupplier(supplierId);
    }
}
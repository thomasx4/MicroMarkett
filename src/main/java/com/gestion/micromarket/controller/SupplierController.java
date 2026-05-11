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

    /**
     * Servicio de proveedores 
     */
    private final SupplierService supplierService;

    /**
     * Crea un nuevo proveedor
     * 
     * @param supplierRequestDTO
     * @return Mensaje de éxito o error
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO> create(@Valid @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            MessageResponseDTO response = supplierService.create(supplierRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al crear el proveedor: " + e.getMessage()));
        }
    }

    /**
     * Obtiene todos los proveedores
     * 
     * @return Lista de proveedores
     */
    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAll() {
        try {
            List<SupplierResponseDTO> response = supplierService.getAll();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Obtiene un proveedor por su ID
     * 
     * @param id
     * @return Proveedor encontrado o error 404 si no existe
     */
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

    /**
     * Actualiza un proveedor existente
     * 
     * @param id
     * @param supplierRequestDTO
     * @return Mensaje de éxito o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            MessageResponseDTO response = supplierService.update(id, supplierRequestDTO);
            if (response.getMessage().equals("El NIT ya está en uso por otro proveedor")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al actualizar el proveedor: " + e.getMessage()));
        }
    }

    /**
     * Elimina un proveedor por su ID
     * 
     * @param id
     * @return Mensaje de éxito o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable Long id) {
        try {
            MessageResponseDTO response = supplierService.delete(id);
            if (response.getMessage().equals("Proveedor no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al eliminar el proveedor: " + e.getMessage()));
        }
    }

    /**
     * Registra una entrada de productos al almacén

     * 
     * @param warehouseEntryDTO
     * @return Mensaje detallado de la operación o error
     */
    @PostMapping("/warehouse-entry")
    public ResponseEntity<MessageResponseDTO> warehouseEntry(@Valid @RequestBody WarehouseEntryDTO warehouseEntryDTO) {
        try {
            MessageResponseDTO response = supplierService.warehouseEntry(warehouseEntryDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al registrar la entrada al almacén: " + e.getMessage()));
        }
    }
}

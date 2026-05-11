package com.gestion.micromarket.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.config.SecurityContext;
import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Supplier;
import com.gestion.micromarket.service.ProductsService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    /** Servicio de prodcutos */
    private final ProductsService service;

    /** Contexto de seguridad y sesión */
    private final SecurityContext security;

    /**
     * Crea un nuevo producto.
     *
     * @param dto Datos del producto a crear
     * @return ResponseEntity con MessageResponseDTO indicando el resultado de la
     *         operación
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO> create(@Valid @RequestBody ProductsRequestDTO dto,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"administrator".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponseDTO("No tienes permiso"));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Obtiene todos los productos activos.
     *
     * @return ResponseEntity con la lista de ProductsResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ProductsResponseDTO>> findAll() {
        if (security.getCurrentRole() == null) {
            throw new RuntimeException("EL rol no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id Identificador del producto
     * @return ResponseEntity con ProductsResponseDTO del producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductsResponseDTO> findById(@PathVariable Long id) {
        if (security.getCurrentRole() == null) {
            throw new RuntimeException("EL rol no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Busca productos por nombre.
     *
     * @param name Nombre o parte del nombre del producto a buscar
     * @return ResponseEntity con la lista de ProductsResponseDTO de los productos
     *         que coinciden
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductsResponseDTO>> findByName(@PathVariable String name) {
        if (security.getCurrentRole() == null) {
            throw new RuntimeException("EL rol no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.findByName(name));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Busca un producto por su código de barras.
     *
     * @param barcode Código de barras del producto
     * @return ResponseEntity con ProductsResponseDTO del producto encontrado
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductsResponseDTO> findByBarcode(@PathVariable String barcode) {
        if (security.getCurrentRole() == null) {
            throw new RuntimeException("EL rol no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.findByBarcode(barcode));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtiene todos los productos activos de una categoría específica.
     *
     * @param categoryId Identificador de la categoría
     * @return ResponseEntity con la lista de ProductsResponseDTO de los productos
     *         de la categoría
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductsResponseDTO>> findByCategory(@PathVariable Long categoryId) {
        if (security.getCurrentRole() == null) {
            throw new RuntimeException("EL rol no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.findByCategory(categoryId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id  Identificador del producto a actualizar
     * @param dto Nuevos datos del producto
     * @return ResponseEntity con MessageResponseDTO indicando el resultado de la
     *         operación
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody ProductsRequestDTO dto) {
        if (!"administrator".equals(security.getCurrentRole())) {
            throw new RuntimeException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.update(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Realiza un borrado lógico de un producto.
     *
     * @param id Identificador del producto a eliminar
     * @return ResponseEntity con MessageResponseDTO indicando el resultado de la
     *         operación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> softDelete(@PathVariable Long id) {
        if (!"administrator".equals(security.getCurrentRole())) {
            throw new RuntimeException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.softDelete(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Restaura un producto previamente eliminado (soft delete).
     *
     * @param id Identificador del producto a restaurar
     * @return ResponseEntity con MessageResponseDTO indicando el resultado de la
     *         operación
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<MessageResponseDTO> restore(@PathVariable Long id) {
        if (!"administrator".equals(security.getCurrentRole())) {
            throw new RuntimeException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }

        try {
            return ResponseEntity.ok(service.restore(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Asocia un proveedor específico a un producto
     * 
     * @param productId  ID del producto
     * @param supplierId ID del proveedor a añadir
     * @return Mensaje de éxito o error en la asociación
     */
    @PostMapping("/{productId}/suppliers/{supplierId}")
    public ResponseEntity<MessageResponseDTO> addSupplier(@PathVariable Long productId,
            @PathVariable Long supplierId) {
        if (!"administrator".equals(security.getCurrentRole())) {
            throw new RuntimeException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.addSupplier(productId, supplierId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Remueve la asociación de un proveedor con un producto
     * 
     * @param productId  ID del producto
     * @param supplierId ID del proveedor a remover
     * @return Mensaje de éxito o error en la desvinculación
     */
    @DeleteMapping("/{productId}/suppliers/{supplierId}")
    public ResponseEntity<MessageResponseDTO> removeSupplier(@PathVariable Long productId,
            @PathVariable Long supplierId) {
        if (!"administrator".equals(security.getCurrentRole())) {
            throw new RuntimeException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.removeSupplier(productId, supplierId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(e.getMessage()));
        }
    }

    /**
     * Obtiene la lista de proveedores asociados a un producto
     * 
     * @param productId ID del producto a consultar
     * @return Conjunto de proveedores vinculados al producto
     */
    @GetMapping("/{productId}/suppliers")
    public ResponseEntity<Set<Supplier>> getSuppliersByProduct(@PathVariable Long productId) {
        if (security.getCurrentRole() == null) {
            throw new RuntimeException("EL rol no esta permitido");
        }
        try {
            return ResponseEntity.ok(service.getSuppliersByProduct(productId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
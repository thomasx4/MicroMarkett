package com.gestion.micromarket.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Supplier;
import com.gestion.micromarket.exception.SecurityAuthorizationException;
import com.gestion.micromarket.service.ProductsService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    /** Servicio de prodcutos */
    private final ProductsService service;

    /**
     * Crea un nuevo producto.
     *
     * @param dto Datos del producto a crear
     * @return ResponseEntity con MessageResponseDTO indicando el resultado de la
     *         operación
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO> create(@Valid @RequestBody ProductsRequestDTO dto) {

        try {
            MessageResponseDTO requestDTO = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(requestDTO);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
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

        try {
            List<ProductsResponseDTO> responseDTOs = service.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
        } catch (SecurityAuthorizationException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            ProductsResponseDTO response = service.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SecurityAuthorizationException e) {

            throw e;
        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            List<ProductsResponseDTO> responseDTOs = service.findByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            ProductsResponseDTO responseDTO = service.findByBarcode(barcode);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            List<ProductsResponseDTO> responseDTOs = service.findByCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            MessageResponseDTO responseDTO = service.update(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            MessageResponseDTO messageResponseDTO = service.softDelete(id);
            return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            MessageResponseDTO responseDTO = service.restore(id);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            MessageResponseDTO responseDTO = service.addSupplier(productId, supplierId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (SecurityAuthorizationException e) {

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO(e.getMessage()));
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

        try {
            MessageResponseDTO responseDTO = service.removeSupplier(productId, supplierId);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

        try {
            Set<Supplier> set = service.getSuppliersByProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(set);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
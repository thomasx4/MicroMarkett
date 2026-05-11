package com.gestion.micromarket.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.*;
import com.gestion.micromarket.repository.*;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository repository;
    private final CategoriesRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    /**
     * Crea un nuevo producto en el sistema.
     *
     * @param dto Objeto con los datos del producto a crear.
     * @return MessageResponseDTO con un mensaje indicando que el producto fue creado correctamente
     * @throws RuntimeException Si el código de barras ya existe o si la categoría especificada no se encuentra
     */
    @Transactional
    public MessageResponseDTO create(ProductsRequestDTO dto) {

        if (repository.existsByBarcode(dto.getBarcode())) {
            throw new RuntimeException("El código de barras ya existe");
        }

        Categories category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Products product = new Products();
        product.setName(dto.getName());
        product.setBarcode(dto.getBarcode());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setActive(dto.getActive());
        product.setCreatedAt(LocalDateTime.now());
        product.setCategory(category);

        repository.save(product);

        return new MessageResponseDTO("Producto creado correctamente");
    }

    /**
     * Obtiene todos los productos activos del sistema.
     *
     * @return Lista de objetos ProductsResponseDTO con los datos de los productos activos
     */
    @Transactional(readOnly = true)
    public List<ProductsResponseDTO> findAll() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id Identificador único del producto
     * @return ProductsResponseDTO con los datos del producto encontrado
     * @throws RuntimeException Si no existe un producto activo con el ID proporcionado
     */
    @Transactional(readOnly = true)
    public ProductsResponseDTO findById(Long id) {
        Products product = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return toDTO(product);
    }

    /**
     * Busca productos por nombre (coincidencia parcial, ignorando mayúsculas/minúsculas).
     *
     * @param name Nombre o parte del nombre del producto a buscar
     * @return Lista de ProductsResponseDTO con los productos activos que coinciden con el nombre
     * @throws RuntimeException Si no se encuentran productos activos con el nombre especificado
     */
    @Transactional(readOnly = true)
    public List<ProductsResponseDTO> findByName(String name) {

        List<Products> products = repository.findByNameContainingIgnoreCaseAndActiveTrue(name);

        if (products.isEmpty()) {
            throw new RuntimeException("No se encontraron productos activos con ese nombre");
        }

        return products.stream().map(this::toDTO).toList();
    }

    /**
     * Busca un producto por su código de barras.
     *
     * @param barcode Código de barras único del producto
     * @return ProductsResponseDTO con los datos del producto encontrado
     * @throws RuntimeException Si no existe un producto activo con el código de barras proporcionado
     */
    @Transactional(readOnly = true)
    public ProductsResponseDTO findByBarcode(String barcode) {

        Products product = repository.findByBarcodeAndActiveTrue(barcode)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ese código de barras"));

        return toDTO(product);
    }

    /**
     * Obtiene todos los productos activos pertenecientes a una categoría específica.
     *
     * @param categoryId Identificador de la categoría
     * @return Lista de ProductsResponseDTO con los productos activos de la categoría
     * @throws RuntimeException Si no hay productos activos en la categoría especificada
     */
    @Transactional(readOnly = true)
    public List<ProductsResponseDTO> findByCategory(Long categoryId) {

        List<Products> products = repository.findByCategoryIdAndActiveTrue(categoryId);

        if (products.isEmpty()) {
            throw new RuntimeException("No hay productos activos en esa categoría");
        }

        return products.stream().map(this::toDTO).toList();
    }

    /**
     * Realiza un borrado lógico de un producto, cambiando su estado activo a falso.
     *
     * @param id Identificador del producto a eliminar
     * @return MessageResponseDTO con un mensaje indicando que el producto fue eliminado correctamente
     * @throws RuntimeException Si no se encuentra un producto con el ID proporcionado
     */
    @Transactional
    public MessageResponseDTO softDelete(Long id) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setActive(false);
        repository.save(product);

        return new MessageResponseDTO("Producto eliminado correctamente");
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id Identificador del producto a actualizar
     * @param dto Objeto con los nuevos datos del producto
     * @return MessageResponseDTO con un mensaje indicando que el producto fue actualizado correctamente
     * @throws RuntimeException Si el producto no existe, si el nuevo código de barras ya está en uso por otro producto, o si la categoría especificada no se encuentra
     */
    @Transactional
    public MessageResponseDTO update(Long id, ProductsRequestDTO dto) {

        Products product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!product.getBarcode().equals(dto.getBarcode()) &&
                repository.existsByBarcode(dto.getBarcode())) {
            throw new RuntimeException("El código de barras ya está en uso");
        }

        Categories category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        product.setName(dto.getName());
        product.setBarcode(dto.getBarcode());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setActive(dto.getActive());
        product.setCategory(category);

        repository.save(product);

        return new MessageResponseDTO("Producto actualizado correctamente");
    }

    /**
     * Restaura un producto previamente eliminado.
     *
     * @param id Identificador del producto a restaurar
     * @return MessageResponseDTO con un mensaje indicando que el producto fue restaurado correctamente
     * @throws RuntimeException Si el producto no existe o si ya se encuentra activo
     */
    @Transactional
    public MessageResponseDTO restore(Long id) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getActive()) {
            throw new RuntimeException("El producto ya está activo");
        }

        product.setActive(true);
        repository.save(product);

        return new MessageResponseDTO("Producto restaurado correctamente");
    }

    /**
     * Asocia un proveedor existente a un producto activo.
     *
     * @param productId  Identificador del producto
     * @param supplierId Identificador del proveedor
     * @return MessageResponseDTO con un mensaje indicando que el proveedor fue agregado correctamente
     * @throws RuntimeException Si el producto no existe o no está activo, o si el proveedor no se encuentra
     */
    @Transactional
    public MessageResponseDTO addSupplier(Long productId, Long supplierId) {
        Products product = repository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        product.getSuppliers().add(supplier);
        repository.save(product);

        return new MessageResponseDTO("Proveedor agregado al producto correctamente");
    }

    /**
     * Elimina la asociación entre un producto activo y un proveedor.
     *
     * @param productId  Identificador del producto
     * @param supplierId Identificador del proveedor
     * @return MessageResponseDTO con un mensaje indicando que el proveedor fue removido correctamente
     * @throws RuntimeException Si el producto no existe o no está activo, o si el proveedor no se encuentra
     */
    @Transactional
    public MessageResponseDTO removeSupplier(Long productId, Long supplierId) {
        Products product = repository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        product.getSuppliers().remove(supplier);
        repository.save(product);

        return new MessageResponseDTO("Proveedor removido del producto correctamente");
    }

    /**
     * Obtiene el conjunto de proveedores asociados a un producto activo.
     *
     * @param productId Identificador del producto
     * @return Set de entidades Supplier asociadas al producto
     * @throws RuntimeException Si el producto no existe o no está activo
     */
    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliersByProduct(Long productId) {
        Products product = repository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return product.getSuppliers();
    }

    /**
     * Convierte una entidad Products a su correspondiente DTO de respuesta.
     *
     * @param product Entidad Products a convertir
     * @return ProductsResponseDTO con los datos del producto
     */
    private ProductsResponseDTO toDTO(Products product) {
        return ProductsResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .barcode(product.getBarcode())
                .price(product.getPrice())
                .stock(product.getStock())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
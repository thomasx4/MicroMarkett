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

    @Transactional(readOnly = true)
    public List<ProductsResponseDTO> findAll() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductsResponseDTO findById(Long id) {
        Products product = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return toDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductsResponseDTO> findByName(String name) {

        List<Products> products = repository.findByNameContainingIgnoreCaseAndActiveTrue(name);

        if (products.isEmpty()) {
            throw new RuntimeException("No se encontraron productos activos con ese nombre");
        }

        return products.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProductsResponseDTO findByBarcode(String barcode) {

        Products product = repository.findByBarcodeAndActiveTrue(barcode)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ese código de barras"));

        return toDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductsResponseDTO> findByCategory(Long categoryId) {

        List<Products> products = repository.findByCategoryIdAndActiveTrue(categoryId);

        if (products.isEmpty()) {
            throw new RuntimeException("No hay productos activos en esa categoría");
        }

        return products.stream().map(this::toDTO).toList();
    }

    @Transactional
    public MessageResponseDTO softDelete(Long id) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Soft delete: cambiar active a false
        product.setActive(false);
        repository.save(product);

        return new MessageResponseDTO("Producto eliminado correctamente");
    }

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

    // Gestion Tabla Puente Product_Supplier
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

    @Transactional(readOnly = true)
    public Set<Supplier> getSuppliersByProduct(Long productId) {
        Products product = repository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return product.getSuppliers();
    }

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
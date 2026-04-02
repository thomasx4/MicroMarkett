package com.gestion.micromarket.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.*;
import com.gestion.micromarket.repository.*;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository repository;
    private final CategoriesRepository categoryRepository;

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

    public List<ProductsResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductsResponseDTO findById(Long id) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return toDTO(product);
    }

    public List<ProductsResponseDTO> findByName(String name) {

        List<Products> products = repository.findByNameContainingIgnoreCase(name);

        if (products.isEmpty()) {
            throw new RuntimeException("No se encontraron productos con ese nombre");
        }

        return products.stream().map(this::toDTO).toList();
    }

    public ProductsResponseDTO findByBarcode(String barcode) {

        Products product = repository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ese código de barras"));

        return toDTO(product);
    }

    public List<ProductsResponseDTO> findByCategory(Long categoryId) {

        List<Products> products = repository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            throw new RuntimeException("No hay productos en esa categoría");
        }

        return products.stream().map(this::toDTO).toList();
    }

    public MessageResponseDTO delete(Long id) {
        Products product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        repository.delete(product);

        return new MessageResponseDTO("Producto eliminado correctamente");
    }

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
}
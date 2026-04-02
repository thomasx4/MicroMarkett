package com.gestion.micromarket.service;

import java.time.LocalDateTime;

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

}
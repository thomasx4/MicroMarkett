package com.gestion.micromarket.service;

import com.gestion.micromarket.dto.CategoriesRequestDTO;
import com.gestion.micromarket.dto.CategoriesResponseDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.entity.Categories;
import com.gestion.micromarket.repository.CategoriesRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }

    public List<CategoriesResponseDTO> getAll(){
        List<Categories> list = categoriesRepository.findAll();
        List<CategoriesResponseDTO> response = new ArrayList<>();

        for(Categories categories : list){
            CategoriesResponseDTO categoriesResponseDTO = new CategoriesResponseDTO();

            categoriesResponseDTO.setId(categories.getId());
            categoriesResponseDTO.setName(categories.getName());
            categoriesResponseDTO.setDescription(categories.getDescription());
            categoriesResponseDTO.setCreatedAt(categories.getCreatedAt());
            response.add(categoriesResponseDTO);
        }
        return response;
    }

    public CategoriesResponseDTO getById(Long id){
        Categories categories = categoriesRepository.findById(id).orElse(null);

        if(categories == null){
            return null;
        }
        CategoriesResponseDTO categoriesResponseDTO = new CategoriesResponseDTO();

        categoriesResponseDTO.setId(categories.getId());
        categoriesResponseDTO.setName(categories.getName());
        categoriesResponseDTO.setDescription(categories.getDescription());
        categoriesResponseDTO.setCreatedAt(categories.getCreatedAt());

        return categoriesResponseDTO;
    }

    // CORRIGE ESTE MÉTODO - Ahora retorna CategoriesResponseDTO en lugar de MessageResponseDTO
    public CategoriesResponseDTO create(CategoriesRequestDTO categoriesRequestDTO){
        // Verificar si ya existe
        if(categoriesRepository.findByName(categoriesRequestDTO.getName()).isPresent()){
            throw new RuntimeException("La categoria ya existe");
        }
        
        Categories categories = new Categories();
        categories.setName(categoriesRequestDTO.getName());
        categories.setDescription(categoriesRequestDTO.getDescription());
        // El createdAt se seteará automáticamente con @PrePersist

        Categories savedCategory = categoriesRepository.save(categories);
        
        // Convertir a response DTO
        CategoriesResponseDTO responseDTO = new CategoriesResponseDTO();
        responseDTO.setId(savedCategory.getId());
        responseDTO.setName(savedCategory.getName());
        responseDTO.setDescription(savedCategory.getDescription());
        responseDTO.setCreatedAt(savedCategory.getCreatedAt());
        
        return responseDTO;
    }

    public MessageResponseDTO update(Long id, CategoriesRequestDTO categoriesRequestDTO){
        Categories categories = categoriesRepository.findById(id).orElse(null);
        
        if(categories == null){
            return new MessageResponseDTO("Categoria no encontrada");
        }
        categories.setName(categoriesRequestDTO.getName());
        categories.setDescription(categoriesRequestDTO.getDescription());
        categoriesRepository.save(categories);
        return new MessageResponseDTO("Categoria actualizada");
    }

    public MessageResponseDTO delete(Long id){
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if(categories == null){
            return new MessageResponseDTO("Categoria no encontrada");
        }
        categoriesRepository.deleteById(id);
        return new MessageResponseDTO("Categoria eliminada");
    }
}
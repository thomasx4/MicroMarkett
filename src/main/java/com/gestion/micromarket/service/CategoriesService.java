package com.gestion.micromarket.service;

import com.gestion.micromarket.config.SecurityContext;
import com.gestion.micromarket.dto.CategoriesRequestDTO;
import com.gestion.micromarket.dto.CategoriesResponseDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.entity.Categories;
import com.gestion.micromarket.enums.Role;
import com.gestion.micromarket.exception.SecurityAuthorizationException;
import com.gestion.micromarket.repository.CategoriesRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    /**
     * Repositorio de categoria
     */
    private final CategoriesRepository categoriesRepository;

    /** Contexto de seguridad y sesión */
    private final SecurityContext security;

    /**
     * Obtiene todas las categorias
     * 
     * @return Lista de categorias convertidas a DTO de respuesta
     */
    public List<CategoriesResponseDTO> getAll() {
        if (!Role.administrator.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        List<Categories> list = categoriesRepository.findAll();
        List<CategoriesResponseDTO> response = new ArrayList<>();

        for (Categories categories : list) {
            CategoriesResponseDTO categoriesResponseDTO = new CategoriesResponseDTO();

            categoriesResponseDTO.setId(categories.getId());
            categoriesResponseDTO.setName(categories.getName());
            categoriesResponseDTO.setDescription(categories.getDescription());
            categoriesResponseDTO.setCreatedAt(categories.getCreatedAt());
            response.add(categoriesResponseDTO);
        }
        return response;
    }

    /**
     * Obtiene una categoria por su Id
     * 
     * @param id
     * @return Categoria convertida a DTO de respuesta y si no existe null
     */
    public CategoriesResponseDTO getById(Long id) {
        if (!Role.administrator.name().equals(security.getCurrentRole())
                && !Role.assistant.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        Categories categories = categoriesRepository.findById(id).orElse(null);

        if (categories == null) {
            return null;
        }
        CategoriesResponseDTO categoriesResponseDTO = new CategoriesResponseDTO();

        categoriesResponseDTO.setId(categories.getId());
        categoriesResponseDTO.setName(categories.getName());
        categoriesResponseDTO.setDescription(categories.getDescription());
        categoriesResponseDTO.setCreatedAt(categories.getCreatedAt());

        return categoriesResponseDTO;
    }

    /**
     * Crea una nueva Categoria
     * 
     * @param categoriesRequestDTO
     * @return Categoria creada convertida a DTO de respuesta
     */
    public CategoriesResponseDTO create(CategoriesRequestDTO categoriesRequestDTO) {

        if (!Role.administrator.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        if (categoriesRepository.findByName(categoriesRequestDTO.getName()).isPresent()) {
            throw new RuntimeException("La categoria ya existe");
        }

        Categories categories = new Categories();
        categories.setName(categoriesRequestDTO.getName());
        categories.setDescription(categoriesRequestDTO.getDescription());

        Categories savedCategory = categoriesRepository.save(categories);

        CategoriesResponseDTO responseDTO = new CategoriesResponseDTO();
        responseDTO.setId(savedCategory.getId());
        responseDTO.setName(savedCategory.getName());
        responseDTO.setDescription(savedCategory.getDescription());
        responseDTO.setCreatedAt(savedCategory.getCreatedAt());

        return responseDTO;
    }

    /**
     * Actualiza una categoria existente
     * 
     * @param id
     * @param categoriesRequestDTO
     * @return Mensaje indicando el resultado de la operacion
     */
    public MessageResponseDTO update(Long id, CategoriesRequestDTO categoriesRequestDTO) {

        if (!Role.administrator.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        Categories categories = categoriesRepository.findById(id).orElse(null);

        if (categories == null) {
            return new MessageResponseDTO("Categoria no encontrada");
        }
        categories.setName(categoriesRequestDTO.getName());
        categories.setDescription(categoriesRequestDTO.getDescription());
        categoriesRepository.save(categories);
        return new MessageResponseDTO("Categoria actualizada");
    }

    /**
     * Elimina una categoria por su Id
     * 
     * @param id
     * @return Mensaje indicando el resultado de la operacion
     */
    public MessageResponseDTO delete(Long id) {
        if (!Role.administrator.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if (categories == null) {
            return new MessageResponseDTO("Categoria no encontrada");
        }
        categoriesRepository.deleteById(id);
        return new MessageResponseDTO("Categoria eliminada");
    }
}
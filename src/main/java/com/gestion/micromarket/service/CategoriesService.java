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
     * Método privado para reutilizar la lógica de validación de administrador.
     */
    private void validateAdminRole() {
        if (!Role.administrator.name().equals(security.getCurrentRole())) {
            throw new SecurityAuthorizationException("EL rol: '" + security.getCurrentRole() + "' no esta permitido");
        }
    }

    /**
     * Obtiene todas las categorias
     * 
     * @return Lista de categorias convertidas a DTO de respuesta
     */
    public List<CategoriesResponseDTO> getAll() {
        validateAdminRole();

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
        Categories categories = categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria no encontrado con el ID: " + id));

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
    public MessageResponseDTO create(CategoriesRequestDTO categoriesRequestDTO) {

        validateAdminRole();

        if (categoriesRepository.findByName(categoriesRequestDTO.getName()).isPresent()) {
            throw new RuntimeException("La categoria ya existe");
        }

        Categories categories = new Categories();
        categories.setName(categoriesRequestDTO.getName());
        categories.setDescription(categoriesRequestDTO.getDescription());

        categoriesRepository.save(categories);

        return new MessageResponseDTO("Categoria creada correctamente");
    }

    /**
     * Actualiza una categoria existente
     * 
     * @param id
     * @param categoriesRequestDTO
     * @return Mensaje indicando el resultado de la operacion
     */
    public MessageResponseDTO update(Long id, CategoriesRequestDTO categoriesRequestDTO) {

        validateAdminRole();

        Categories categories = categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria no encontrado con ID: " + id));

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
        validateAdminRole();

        Categories categories = categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Cateogria no encontrado con el id: " + id));

        categoriesRepository.delete(categories);
        return new MessageResponseDTO("Categoria eliminada");
    }
}
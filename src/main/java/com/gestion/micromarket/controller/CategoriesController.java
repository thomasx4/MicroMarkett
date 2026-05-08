package com.gestion.micromarket.controller;

import com.gestion.micromarket.dto.CategoriesRequestDTO;
import com.gestion.micromarket.dto.CategoriesResponseDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.service.CategoriesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")  
public class CategoriesController {
    /**
     * Servicio de categoria
     */
    private final CategoriesService categoriesService;

    /**
     * Obtiene todas las categorias
     * 
     * @return Lista de categorias
     */
    @GetMapping
    public ResponseEntity<List<CategoriesResponseDTO>> getAll(){
        try {
            List<CategoriesResponseDTO> list = categoriesService.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Obtiene una categoria por su Id 
     * 
     * @param id
     * @return Categoria encontrada o si no existe error 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriesResponseDTO> getById(@PathVariable Long id){
        try {
            CategoriesResponseDTO reponse = categoriesService.getById(id);
            if(reponse == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(reponse);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Crea una nueva Categoria
     * 
     * @param categoriesRequestDTO
     * @return Categoria creada o mensaje de error 
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoriesRequestDTO categoriesRequestDTO){
        try {
            CategoriesResponseDTO response = categoriesService.create(categoriesRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponseDTO(e.getMessage()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al crear la categoria: " + e.getMessage()));
        }
    }

    /**
     * Actualiza una categoria
     * 
     * @param id
     * @param categoriesRequestDTO
     * @return Mensaje de exito o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriesRequestDTO categoriesRequestDTO){
        try {
            MessageResponseDTO messageResponseDTO = categoriesService.update(id, categoriesRequestDTO);
            if(messageResponseDTO.getMessage().equals("Categoria no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponseDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al actualizar la categoria: " + e.getMessage()));
        }
    }

    /**
     * Elimina una categoria por si Id
     * 
     * @param id
     * @return Mensaje de exito error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable Long id){
        try {
            MessageResponseDTO messageResponseDTO = categoriesService.delete(id);
            if(messageResponseDTO.getMessage().equals("Categoria no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponseDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponseDTO("Error al eliminar la categoria: " + e.getMessage()));
        }
    }
}
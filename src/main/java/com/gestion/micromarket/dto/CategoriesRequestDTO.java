package com.gestion.micromarket.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriesRequestDTO {

    @NotBlank(message = "El nombre de la categoria es obligatoria")
    private String name;

    @NotBlank(message = "La descripcion de la categoria es obligatoria")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}



package com.gestion.micromarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gestion.micromarket.entity.Categories;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Optional<Categories> findByName(String name);

}
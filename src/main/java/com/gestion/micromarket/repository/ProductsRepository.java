package com.gestion.micromarket.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestion.micromarket.entity.Products;
import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    /**
     * Verifica si ya existe un producto con el código de barras especificado.
     *
     * @param barcode Código de barras a verificar
     * @return true si existe un producto con ese código de barras, false en caso contrario
     */
    boolean existsByBarcode(String barcode);
    
    /**
     * Verifica si ya existe un producto activo con el código de barras especificado.
     *
     * @param barcode Código de barras a verificar
     * @return true si existe un producto activo con ese código de barras, false en caso contrario
     */
    boolean existsByBarcodeAndActiveTrue(String barcode);

    /**
     * Busca un producto por su código de barras (sin importar el estado activo).
     *
     * @param barcode Código de barras del producto
     * @return Optional que contiene el producto si se encuentra, o vacío si no existe
     */
    Optional<Products> findByBarcode(String barcode);
    
    /**
     * Busca un producto activo por su código de barras.
     *
     * @param barcode Código de barras del producto
     * @return Optional que contiene el producto activo si se encuentra, o vacío si no existe
     */
    Optional<Products> findByBarcodeAndActiveTrue(String barcode);

    /**
     * Busca productos por nombre (coincidencia parcial, ignorando mayúsculas/minúsculas),
     * sin importar el estado activo.
     *
     * @param name Nombre o parte del nombre del producto a buscar
     * @return Lista de productos que coinciden con el nombre especificado
     */
    List<Products> findByNameContainingIgnoreCase(String name);
    
    /**
     * Busca productos activos por nombre (coincidencia parcial, ignorando mayúsculas/minúsculas).
     *
     * @param name Nombre o parte del nombre del producto a buscar
     * @return Lista de productos activos que coinciden con el nombre especificado
     */
    List<Products> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    /**
     * Busca productos por ID de categoría (sin importar el estado activo).
     *
     * @param categoryId Identificador de la categoría
     * @return Lista de productos pertenecientes a la categoría especificada
     */
    List<Products> findByCategoryId(Long categoryId);
    
    /**
     * Busca productos activos por ID de categoría.
     *
     * @param categoryId Identificador de la categoría
     * @return Lista de productos activos pertenecientes a la categoría especificada
     */
    List<Products> findByCategoryIdAndActiveTrue(Long categoryId);
    
    /**
     * Obtiene todos los productos activos del sistema.
     *
     * @return Lista de todos los productos con estado active = true
     */
    List<Products> findAllByActiveTrue();
    
    /**
     * Busca un producto activo por su ID.
     *
     * @param id Identificador único del producto
     * @return Optional que contiene el producto activo si se encuentra, o vacío si no existe
     */
    Optional<Products> findByIdAndActiveTrue(Long id);
    
    /**
     * Busca un producto por su ID (sin importar el estado activo).
     * Este método sobrescribe el método findById de JpaRepository para mayor claridad.
     *
     * @param id Identificador único del producto
     * @return Optional que contiene el producto si se encuentra, o vacío si no existe
     */
    Optional<Products> findById(Long id);
}
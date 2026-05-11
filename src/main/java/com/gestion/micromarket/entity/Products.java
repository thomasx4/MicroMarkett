package com.gestion.micromarket.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table; 
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Products {

    /**
     * Identificador único del producto.
     * Se genera automáticamente usando la estrategia de auto-incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del producto.
     * No puede ser nulo y tiene una longitud máxima de 50 caracteres.
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Código de barras único del producto.
     * No puede ser nulo, es único y tiene una longitud máxima de 50 caracteres.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String barcode;

    /**
     * Precio del producto.
     * No puede ser nulo. Utiliza BigDecimal para representar valores monetarios con precisión.
     */
    @Column(nullable = false) 
    private BigDecimal price; 

    /**
     * Cantidad de unidades disponibles en inventario.
     * No puede ser nulo.
     */
    @Column(nullable = false)
    private Long stock;

    /**
     * Estado del producto (activo/inactivo).
     * No puede ser nulo. false indica que el producto fue eliminado lógicamente.
     */
    @Column(nullable = false)
    private Boolean active;

    /**
     * Fecha y hora de creación del producto.
     * No se actualiza después de la inserción inicial.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Categoría a la que pertenece el producto.
     * Relación muchos-a-uno con la entidad Categories.
     * Se carga de forma perezosa (LAZY) para optimizar el rendimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "category_id") 
    private Categories category;

    /**
     * proveedores que suministran este producto.
     * Relación muchos-a-muchos con la entidad Supplier.
     * Está mapeada por el atributo "products" en la entidad Supplier.
     * Se carga de forma perezosa (LAZY) y se inicializa con un HashSet vacío.
     */
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Set<Supplier> suppliers = new HashSet<>();

    /**
     * Método de ciclo de vida que se ejecuta automáticamente antes de persistir la entidad.
     * Establece la fecha y hora actual en el campo createdAt.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

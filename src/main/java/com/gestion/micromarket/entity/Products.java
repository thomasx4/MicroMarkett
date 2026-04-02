package com.gestion.micromarket.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table; 
import lombok.Data;

@Entity
@Table(name = "products") 
@Data public class Products { 

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    @Column(nullable = false, length = 50) 
    private String name; 

    @Column(nullable = false, unique = true, length = 50) 
    private String barcode; 

    @Column(nullable = false) 
    private Double price; 

    @Column(nullable = false) 
    private Long stock; 

    @Column(nullable = false) 
    private Boolean active; 

    @Column(name = "created_at", updatable = false) 
    private LocalDateTime createdAt; 

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "category_id") 
    private Categories category; 
}

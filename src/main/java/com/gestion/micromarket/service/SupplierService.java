package com.gestion.micromarket.service;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.entity.Supplier;
import com.gestion.micromarket.repository.SupplierRepository;
import com.gestion.micromarket.repository.ProductsRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ProductsRepository productsRepository;

    public SupplierService(SupplierRepository supplierRepository, ProductsRepository productsRepository){
        this.supplierRepository = supplierRepository;
        this.productsRepository = productsRepository;
    }

    public List<SupplierResponseDTO> getAll(){
        List<Supplier> list = supplierRepository.findAll();
        List<SupplierResponseDTO> response = new ArrayList<>();

        for(Supplier supplier : list){
            SupplierResponseDTO supplierResponseDTO = new SupplierResponseDTO();
            supplierResponseDTO.setId(supplier.getId());
            supplierResponseDTO.setName(supplier.getName());
            supplierResponseDTO.setTaxId(supplier.getTaxId());
            supplierResponseDTO.setPhone(supplier.getPhone());
            supplierResponseDTO.setAddress(supplier.getAddress());
            supplierResponseDTO.setEmail(supplier.getEmail());
            supplierResponseDTO.setCreatedAt(supplier.getCreatedAt());
            response.add(supplierResponseDTO);
        }

        return response;
    }

    public SupplierResponseDTO getById(Long id){
        Supplier supplier = supplierRepository.findById(id).orElse(null);

        if(supplier == null){
            return null;
        }
        SupplierResponseDTO supplierResponseDTO = new SupplierResponseDTO();
        supplierResponseDTO.setId(supplier.getId());
        supplierResponseDTO.setName(supplier.getName());
        supplierResponseDTO.setTaxId(supplier.getTaxId());  
        supplierResponseDTO.setPhone(supplier.getPhone());
        supplierResponseDTO.setAddress(supplier.getAddress());
        supplierResponseDTO.setEmail(supplier.getEmail());
        supplierResponseDTO.setCreatedAt(supplier.getCreatedAt());

        return supplierResponseDTO;
    }

    public MessageResponseDTO create(SupplierRequestDTO supplierRequestDTO){

    if(supplierRepository.findByTaxId(supplierRequestDTO.getTaxId()).isPresent()){
        return new MessageResponseDTO("El proveedor ya existe");
    }

        Supplier supplier = new Supplier();
        supplier.setName(supplierRequestDTO.getName());
        supplier.setTaxId(supplierRequestDTO.getTaxId());
        supplier.setPhone(supplierRequestDTO.getPhone());
        supplier.setAddress(supplierRequestDTO.getAddress());
        supplier.setEmail(supplierRequestDTO.getEmail());

        supplierRepository.save(supplier);

        return new MessageResponseDTO("Proveedor creado correctamente");
    }

    public MessageResponseDTO update(Long id, SupplierRequestDTO dto){

        Supplier supplier = supplierRepository.findById(id).orElse(null);

        if(supplier == null){
            return new MessageResponseDTO("Proveedor no encontrado");
        }

        if(!supplier.getTaxId().equals(dto.getTaxId())){
            if(supplierRepository.findByTaxId(dto.getTaxId()).isPresent()){
                return new MessageResponseDTO("El NIT ya está en uso");
            }
        }

        supplier.setName(dto.getName());
        supplier.setTaxId(dto.getTaxId());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setEmail(dto.getEmail());

        supplierRepository.save(supplier);

        return new MessageResponseDTO("Proveedor actualizado correctamente");
    }

    public MessageResponseDTO delete(Long id){
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if(supplier == null){
            return new MessageResponseDTO("Proveedor no encontrado");
        }
        supplierRepository.deleteById(id);
        return new MessageResponseDTO("Proveedor eliminado");
    }

    // RELACION DE PROVEEDOR Y PRODUCTO

    // AGREGAR PRODUCTO A PROVEEDOR

    @Transactional
    public MessageResponseDTO addProduct(Long supplierId, Long productId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        supplier.getProducts().add(product);
        supplierRepository.save(supplier);

        return new MessageResponseDTO("Producto agregado al proveedor correctamente");
    }

    // QUITAR PRODUCTO

    @Transactional
    public MessageResponseDTO removeProduct(Long supplierId, Long productId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        supplier.getProducts().remove(product);

        supplierRepository.save(supplier);
        return new MessageResponseDTO("Producto removido del proveedor correctamente");
    }

    // OBTENER PRODUCOS DE UN PROVEEDOR

    @Transactional(readOnly = true)
    public Set<Products> getProductsBySupplier(Long supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        return supplier.getProducts();
    }

}
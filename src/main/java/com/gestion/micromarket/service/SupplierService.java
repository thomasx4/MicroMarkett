package com.gestion.micromarket.service;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.entity.Supplier;
import com.gestion.micromarket.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

import com.gestion.micromarket.repository.ProductsRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    /**
     * Repositorio de proveedores
     */
    private final SupplierRepository supplierRepository;

    /**
     * Repositorio de productos
     */
    private final ProductsRepository productsRepository;


    /**
     * Obtiene todos los proveedores
     * 
     * @return Lista de proveedores convertidos a DTO de rerspuesta
     */
    public List<SupplierResponseDTO> getAll() {
        List<Supplier> list = supplierRepository.findAll();
        List<SupplierResponseDTO> response = new ArrayList<>();

        for (Supplier supplier : list) {
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

    /**
     * Obtiene proveedor por Id 
     * 
     * @param id
     * @return Proveedor convertido a proveedor de repuesta
     */
    public SupplierResponseDTO getById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
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


    /**
     * Crea un nuevo proveedor
     * 
     * @param supplierRequestDTO
     * @return Mensaje indicando el resultad de la operacion
     */
    public MessageResponseDTO create(SupplierRequestDTO supplierRequestDTO) {

        if (supplierRequestDTO.getTaxId() == null) {
            return new MessageResponseDTO("El NIT es obligatorio, debe ingresarlo");
        }

        if (supplierRepository.findByTaxId(supplierRequestDTO.getTaxId()).isPresent()) {
            return new MessageResponseDTO("El NIT ya existe, no puede repetirse: " + supplierRequestDTO.getTaxId());
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

    /**
     * Actualiza un proveedor existente
     * 
     * @param id
     * @param dto
     * @return Mensaje indicando el resulado de la operacion
     */
    public MessageResponseDTO update(Long id, SupplierRequestDTO dto) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El proveedor no se encunetra con el id: " + id));

        if (dto.getTaxId() != null && !dto.getTaxId().trim().isEmpty()) {

            if (supplier.getTaxId() == null || !supplier.getTaxId().equals(dto.getTaxId())) {

                if (supplierRepository.findByTaxId(dto.getTaxId()).isPresent()) {
                    return new MessageResponseDTO("El NIT ya está en uso por otro proveedor");
                }
            }
            supplier.setTaxId(dto.getTaxId());
        }

        if (dto.getName() != null) {
            supplier.setName(dto.getName());
        }

        if (dto.getPhone() != null) {
            supplier.setPhone(dto.getPhone());
        }

        if (dto.getAddress() != null) {
            supplier.setAddress(dto.getAddress());
        }

        if (dto.getEmail() != null) {
            supplier.setEmail(dto.getEmail());
        }

        supplierRepository.save(supplier);

        return new MessageResponseDTO("Proveedor actualizado correctamente");

    }


    /**
     * Elimina un proveedor por su Id
     * 
     * @param id
     * @return Mensaje indicando el resultado de la operacion
     */
    public MessageResponseDTO delete(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier == null) {
            return new MessageResponseDTO("Proveedor no encontrado");
        }
        supplierRepository.deleteById(id);
        return new MessageResponseDTO("Proveedor eliminado");
    }

    /**
     * Agrega un producto a un proveedor
     * 
     * @param supplierId
     * @param productId
     * @return Mensaje indicando el resultado de la operacion
     */
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

    /**
     * Remueve un producto a un proveedor
     *  
     * @param supplierId
     * @param productId
     * @return Mensaje indicando el resultado de la operacion
     */
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


    /**
     * Obtiene todos los productos asociadfos a un proveedor
     * 
     * @param supplierId
     * @return Conjunto de productos del proveedor
     */
    @Transactional(readOnly = true)
    public Set<Products> getProductsBySupplier(Long supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        return supplier.getProducts();
    }


    /**
     * Registra una entrada de productos al almacén, aumentando el stock
     * 
     * @param dto
     * @return Mensaje detallado con la información de la operación
     */
    @Transactional
    public MessageResponseDTO warehouseEntry(WarehouseEntryDTO dto) {

        /**
         * Buscar producto
         */
        Products product = productsRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dto.getProductId()));

        /**
         * Buscar proveedor
         */
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + dto.getSupplierId()));

        /**
         * Validar cantidad
         */
        if (dto.getQuantity() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        /**
         * Sumar stock
         */
        Long stockAnterior = product.getStock();
        Long nuevoStock = stockAnterior + dto.getQuantity();
        product.setStock(nuevoStock);
        productsRepository.save(product);

        return new MessageResponseDTO("Entrada de almacen realizada correctamente: "
                + "Producto: " + product.getName() + ","
                + "Stock anterior: " + stockAnterior + ", "
                + "Cantidad agregada: " + dto.getQuantity() + ", "
                + "Proveedor: " + supplier.getName() + ", "
                + "Nuevo stock: " + nuevoStock);
    }

}
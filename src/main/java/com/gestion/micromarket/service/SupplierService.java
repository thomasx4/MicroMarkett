package com.gestion.micromarket.service;

import com.gestion.micromarket.dto.*;
import com.gestion.micromarket.entity.Supplier;
import com.gestion.micromarket.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
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
}
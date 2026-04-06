package com.gestion.micromarket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gestion.micromarket.dto.EmployeeInfoDTO;
import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.SaleDetailRequestDTO;
import com.gestion.micromarket.dto.SaleDetailResponseDTO;
import com.gestion.micromarket.dto.SalesRequestDTO;
import com.gestion.micromarket.dto.SalesResponseDTO;
import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.entity.SaleDetail;
import com.gestion.micromarket.entity.Sales;
import com.gestion.micromarket.repository.EmployeesRepository;
import com.gestion.micromarket.repository.ProductsRepository;
import com.gestion.micromarket.repository.SaleDetailRepository;
import com.gestion.micromarket.repository.SaleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final EmployeesRepository employeesRepository;
    private final ProductsRepository productRepository;

    private static final BigDecimal IVA_RATE = new BigDecimal("0.19");

    // ------------------------------- CREATE -------------------------------
    @Transactional
    public MessageResponseDTO createSale(SalesRequestDTO salesRequestDTO) {

        Employees employee = employeesRepository.findById(salesRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con ID: " + salesRequestDTO.getEmployeeId()));

        for (SaleDetailRequestDTO detailReq : salesRequestDTO.getSaleDetails()) {
            Products product = productRepository.findById(detailReq.getProductId())
                    .orElseThrow(
                            () -> new RuntimeException("Producto no encontrado con ID: " + detailReq.getProductId()));

            if (product.getStock() < detailReq.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getName() +
                        ". Stock disponible: " + product.getStock());
            }
        }

        Sales sale = new Sales();
        sale.setEmployee(employee);
        sale.setSaleDate(LocalDateTime.now());
        sale.setSubtotal(BigDecimal.ZERO);
        sale.setVat(BigDecimal.ZERO);
        sale.setTotal(BigDecimal.ZERO);

        Sales savedSale = saleRepository.save(sale);

        BigDecimal subtotalTotal = BigDecimal.ZERO;

        for (SaleDetailRequestDTO detailReq : salesRequestDTO.getSaleDetails()) {
            Products product = productRepository.findById(detailReq.getProductId()).get();

            SaleDetail detail = new SaleDetail();
            detail.setSale(savedSale);
            detail.setProduct(product);
            detail.setQuantity(detailReq.getQuantity());
            detail.setUnitPrice(detailReq.getUnitPrice());

            BigDecimal subtotalDetalle = detailReq.getUnitPrice()
                    .multiply(BigDecimal.valueOf(detailReq.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            detail.setSubtotal(subtotalDetalle);

            subtotalTotal = subtotalTotal.add(subtotalDetalle);
            saleDetailRepository.save(detail);

            product.setStock(product.getStock() - detailReq.getQuantity());
            productRepository.save(product);
        }

        BigDecimal vat = subtotalTotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalTotal.add(vat).setScale(2, RoundingMode.HALF_UP);

        savedSale.setSubtotal(subtotalTotal);
        savedSale.setVat(vat);
        savedSale.setTotal(total);
        saleRepository.save(savedSale);

        return new MessageResponseDTO("Venta creada exitosamente con ID: " + savedSale.getId());
    }

    // ------------------------------- GET ALL -------------------------------
    public List<SalesResponseDTO> getAllSales() {
        List<Sales> sales = saleRepository.findAll();
        List<SalesResponseDTO> listSales = new ArrayList<>();

        for (Sales sale : sales) {
            listSales.add(convertToResponseDTO(sale));
        }

        return listSales;
    }

    // ------------------------------- GET BY ID -------------------------------
    public Optional<SalesResponseDTO> getSaleById(Long id) {
        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        return Optional.of(convertToResponseDTO(sale));
    }

    // ------------------------------- GET BY EMPLOYEE ID
    // -------------------------------
    public List<SalesResponseDTO> getSalesByEmployeeId(Long employeeId) {
        List<Sales> sales = saleRepository.findByEmployeeId(employeeId);

        if (sales.isEmpty()) {
            throw new RuntimeException("No se encontraron ventas para el empleado con ID: " + employeeId);
        }

        return sales.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ------------------------------- UPDATE -------------------------------
    @Transactional
    public SalesResponseDTO updateSale(Long id, SalesRequestDTO salesRequestDTO) {
        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        // Actualizar empleado si viene
        if (salesRequestDTO.getEmployeeId() != null) {
            Employees employee = employeesRepository.findById(salesRequestDTO.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Empleado no encontrado con ID: " + salesRequestDTO.getEmployeeId()));
            sale.setEmployee(employee);
        }

        if (salesRequestDTO.getSaleDetails() != null && !salesRequestDTO.getSaleDetails().isEmpty()) {

            List<SaleDetail> oldDetails = saleDetailRepository.findBySaleId(id);
            for (SaleDetail oldDetail : oldDetails) {
                Products product = oldDetail.getProduct();
                product.setStock(product.getStock() + oldDetail.getQuantity());
                productRepository.save(product);
                saleDetailRepository.delete(oldDetail);
            }

            BigDecimal subtotalTotal = BigDecimal.ZERO;

            for (SaleDetailRequestDTO detailReq : salesRequestDTO.getSaleDetails()) {
                Products product = productRepository.findById(detailReq.getProductId())
                        .orElseThrow(() -> new RuntimeException(
                                "Producto no encontrado con ID: " + detailReq.getProductId()));

                if (product.getStock() < detailReq.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
                }

                SaleDetail detail = new SaleDetail();
                detail.setSale(sale);
                detail.setProduct(product);
                detail.setQuantity(detailReq.getQuantity());
                detail.setUnitPrice(detailReq.getUnitPrice());

                BigDecimal subtotalDetalle = detailReq.getUnitPrice()
                        .multiply(BigDecimal.valueOf(detailReq.getQuantity()))
                        .setScale(2, RoundingMode.HALF_UP);
                detail.setSubtotal(subtotalDetalle);
                subtotalTotal = subtotalTotal.add(subtotalDetalle);

                saleDetailRepository.save(detail);

                product.setStock(product.getStock() - detailReq.getQuantity());
                productRepository.save(product);
            }

            BigDecimal vat = subtotalTotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
            BigDecimal total = subtotalTotal.add(vat).setScale(2, RoundingMode.HALF_UP);

            sale.setSubtotal(subtotalTotal);
            sale.setVat(vat);
            sale.setTotal(total);
        }

        Sales updatedSale = saleRepository.save(sale);
        return convertToResponseDTO(updatedSale);
    }

    // ------------------------------- DELETE -------------------------------
    @Transactional
    public MessageResponseDTO deleteSaleById(Long id) {
        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        List<SaleDetail> details = saleDetailRepository.findBySaleId(id);
        for (SaleDetail detail : details) {
            Products product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        }

        saleDetailRepository.deleteAll(details);
        saleRepository.delete(sale);

        return new MessageResponseDTO("Venta eliminada correctamente 😀");
    }

    private SalesResponseDTO convertToResponseDTO(Sales sale) {
        SalesResponseDTO response = new SalesResponseDTO();
        response.setId(sale.getId());
        response.setSaleDate(sale.getSaleDate());
        response.setSubtotal(sale.getSubtotal());
        response.setVat(sale.getVat());
        response.setTotal(sale.getTotal());

        EmployeeInfoDTO employeeInfo = new EmployeeInfoDTO();
        employeeInfo.setId(sale.getEmployee().getId());
        employeeInfo.setName(sale.getEmployee().getName());
        employeeInfo.setDocumentNumber(sale.getEmployee().getDocumentNumber());
        employeeInfo.setRole(sale.getEmployee().getRole().name());
        employeeInfo.setActive(sale.getEmployee().getActive());
        response.setEmployee(employeeInfo);

        List<SaleDetail> details = saleDetailRepository.findBySaleId(sale.getId());
        List<SaleDetailResponseDTO> detailDTOs = new ArrayList<>();

        for (SaleDetail detail : details) {
            SaleDetailResponseDTO detailDTO = new SaleDetailResponseDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setProductId(detail.getProduct().getId());
            detailDTO.setProductName(detail.getProduct().getName());
            detailDTO.setQuantity(detail.getQuantity());
            detailDTO.setUnitPrice(detail.getUnitPrice());
            detailDTO.setSubtotal(detail.getSubtotal());
            detailDTOs.add(detailDTO);
        }

        response.setSaleDetails(detailDTOs);
        return response;
    }
}
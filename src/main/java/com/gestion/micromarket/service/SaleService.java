package com.gestion.micromarket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.SaleDetailRequestDTO;
import com.gestion.micromarket.dto.SalesRequestDTO;
import com.gestion.micromarket.dto.SalesResponseDTO;
import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.entity.SaleDetail;
import com.gestion.micromarket.entity.Sales;
import com.gestion.micromarket.entity.enums.Role;
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
                        "El empleado no se encuentra con el ID: " + salesRequestDTO.getEmployeeId()));

        if (!employee.getActive()) {
            throw new RuntimeException("El empleado no esta activo con el ID " + salesRequestDTO.getEmployeeId());

        }

        if (employee.getRole() != Role.cashier) {
            throw new RuntimeException(
                    "El empleado no puede realizar la venta, solo el Cajero lo puede realizar ya que el rol del empleado es: "
                            + employee.getRole());
        }

        if (salesRequestDTO.getSaleDetails() == null || salesRequestDTO.getSaleDetails().isEmpty()) {
            throw new RuntimeException("La venta debe de tener por lo menos un producto");
        }

        

        for (SaleDetailRequestDTO saleDetailRequestDTO : salesRequestDTO.getSaleDetails()) {
            Products product = productRepository.findById(saleDetailRequestDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "El producto no se encuentra con el ID: " + saleDetailRequestDTO.getProductId()));

            if (!product.getActive()) {
                throw new RuntimeException("El producto: " + product.getName() + " no se encuentra activo");
            }

            if (product.getStock() < saleDetailRequestDTO.getQuantity()) {
                throw new RuntimeException("La cantidad que pusiste no es suficiente: " + saleDetailRequestDTO.getQuantity()+ " - solo hay disponible: "+ product.getStock());

            }
        }

        Sales sale = new Sales();
        sale.setEmployee(employee);
        sale.setSubtotal(BigDecimal.ZERO);
        sale.setVat(BigDecimal.ZERO);
        sale.setTotal(BigDecimal.ZERO);

        saleRepository.save(sale);

        BigDecimal subTotalTodo = BigDecimal.ZERO;
        List<SaleDetail> saleDetail = new ArrayList<>();

        for (SaleDetailRequestDTO saleDetailRequestDTO : salesRequestDTO.getSaleDetails()) {
            Products product = productRepository.findById(saleDetailRequestDTO.getProductId()).get();

            BigDecimal unitPrice = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(saleDetailRequestDTO.getQuantity());

            BigDecimal subtotalDetalle = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

            subTotalTodo = subTotalTodo.add(subtotalDetalle);

            SaleDetail saleDetailCreate = new SaleDetail();
            saleDetailCreate.setSale(sale);
            saleDetailCreate.setProduct(product);
            saleDetailCreate.setQuantity(saleDetailRequestDTO.getQuantity());
            saleDetailCreate.setUnitPrice(unitPrice);

            saleDetail.add(saleDetailCreate);

        }
        saleDetailRepository.saveAll(saleDetail);

        BigDecimal vat = subTotalTodo.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subTotalTodo.add(vat).setScale(2, RoundingMode.HALF_UP);

        sale.setSubtotal(subTotalTodo);
        sale.setVat(vat);
        sale.setTotal(total);
        saleRepository.save(sale);

        for (SaleDetailRequestDTO saleDetailRequestDTO : salesRequestDTO.getSaleDetails()) {
            Products product = productRepository.findById(saleDetailRequestDTO.getProductId()).get();

            Long nuevoStock = product.getStock() - saleDetailRequestDTO.getQuantity();
            product.setStock(nuevoStock);
            productRepository.save(product);
        }

        return new MessageResponseDTO("La venta fue creada exitosamente con el ID: " + sale.getId());
    }

    // ------------------------------- GET BY ID -------------------------------

    


}
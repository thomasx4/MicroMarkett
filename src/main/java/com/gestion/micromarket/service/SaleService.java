package com.gestion.micromarket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.SaleDetailRequestDTO;
import com.gestion.micromarket.dto.SaleDetailResponseDTO;
import com.gestion.micromarket.dto.SalesRequestDTO;
import com.gestion.micromarket.dto.SalesResponseDTO;
import com.gestion.micromarket.entity.Employees;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.entity.SaleDetail;
import com.gestion.micromarket.entity.Sales;
import com.gestion.micromarket.enums.Role;
import com.gestion.micromarket.repository.EmployeesRepository;
import com.gestion.micromarket.repository.ProductsRepository;
import com.gestion.micromarket.repository.SaleDetailRepository;
import com.gestion.micromarket.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final EmployeesRepository employeesRepository;
    private final ProductsRepository productRepository;

    private static final BigDecimal IVA_RATE = new BigDecimal("0.19");

    /**
     * Convierte una venta a su formato de respuesta
     *
     * @param sale La venta a convertir
     * @return Los datos de la venta listos para enviar
     */
    private SalesResponseDTO listToResponseDTO(Sales sale) {
        SalesResponseDTO salesResponseDTO = new SalesResponseDTO();
        salesResponseDTO.setId(sale.getId());
        salesResponseDTO.setSaleDate(sale.getSaleDate());
        salesResponseDTO.setSubtotal(sale.getSubtotal());
        salesResponseDTO.setVat(sale.getVat());
        salesResponseDTO.setTotal(sale.getTotal());
        salesResponseDTO.setEmployeeId(sale.getEmployee().getId());
        salesResponseDTO.setEmployeeName(sale.getEmployee().getName());
        salesResponseDTO.setEmployeeRole(sale.getEmployee().getRole());
        salesResponseDTO.setEmployeeActive(sale.getEmployee().getActive());

        List<SaleDetail> saleDetails = saleDetailRepository.findBySaleId(sale.getId());
        List<SaleDetailResponseDTO> saleDetailResponseDTOs = new ArrayList<>();

        for (SaleDetail saleDetail : saleDetails) {
            SaleDetailResponseDTO saleDetailResponseDTO = new SaleDetailResponseDTO();
            saleDetailResponseDTO.setId(saleDetail.getId());
            saleDetailResponseDTO.setProductId(saleDetail.getProduct().getId());
            saleDetailResponseDTO.setProductName(saleDetail.getProduct().getName());
            saleDetailResponseDTO.setProductBarcode(saleDetail.getProduct().getBarcode());
            saleDetailResponseDTO.setQuantity(saleDetail.getQuantity());
            saleDetailResponseDTO.setUnitPrice(saleDetail.getUnitPrice());
            saleDetailResponseDTOs.add(saleDetailResponseDTO);
        }

        salesResponseDTO.setSaleDetails(saleDetailResponseDTOs);
        return salesResponseDTO;

    }

    
    /**
     * Crea una nueva venta
     *
     * @param salesRequestDTO Datos de la venta (empleado, productos, cantidades)
     * @return Mensaje confirmando que la venta fue creada
     */
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
                throw new RuntimeException("La cantidad que pusiste no es suficiente: "
                        + saleDetailRequestDTO.getQuantity() + " - solo hay disponible: " + product.getStock());

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

    
    /**
     * Obtiene todas las ventas registradas
     *
     * @return Lista de todas las ventas
     */
    public List<SalesResponseDTO> getAllSales() {
        List<Sales> sales = saleRepository.findAll();
        List<SalesResponseDTO> listSales = new ArrayList<>();

        for (Sales sale : sales) {
            listSales.add(listToResponseDTO(sale));
        }

        return listSales;
    }

    
    /**
     * Busca una venta por su ID
     *
     * @param id El número de identificación de la venta
     * @return Los datos de la venta encontrada
     */
    @Transactional(readOnly = true)
    public SalesResponseDTO getSaleById(Long id) {
        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La venta no se encuentra con el id:  " + id));

        return listToResponseDTO(sale);
    }

    
    /**
     * Actualiza los datos de una venta existente
     *
     * @param id El ID de la venta a actualizar
     * @param salesRequestDTO Los nuevos datos de la venta
     * @return Los datos actualizados de la venta
     */
    @Transactional
    public SalesResponseDTO updateSale(Long id, SalesRequestDTO salesRequestDTO) {

        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        Employees employee = employeesRepository.findById(salesRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con ID: " + salesRequestDTO.getEmployeeId()));
        sale.setEmployee(employee);

        List<SaleDetail> oldsaleDetails = saleDetailRepository.findBySaleId(id);
        saleDetailRepository.deleteAll(oldsaleDetails);

        BigDecimal subtotalTotal = BigDecimal.ZERO;
        List<SaleDetail> newsaleDetails = new ArrayList<>();

        for (SaleDetailRequestDTO saleDetailRequestDTO : salesRequestDTO.getSaleDetails()) {
            Products product = productRepository.findById(saleDetailRequestDTO.getProductId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Producto no encontrado con ID: " + saleDetailRequestDTO.getProductId()));

            BigDecimal subtotalDetalle = product.getPrice()
                    .multiply(BigDecimal.valueOf(saleDetailRequestDTO.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            subtotalTotal = subtotalTotal.add(subtotalDetalle);

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setProduct(product);
            detail.setQuantity(saleDetailRequestDTO.getQuantity());
            detail.setUnitPrice(product.getPrice());

            newsaleDetails.add(detail);
        }

        saleDetailRepository.saveAll(newsaleDetails);

        BigDecimal vat = subtotalTotal.multiply(new BigDecimal("0.19")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalTotal.add(vat).setScale(2, RoundingMode.HALF_UP);

        sale.setSubtotal(subtotalTotal);
        sale.setVat(vat);
        sale.setTotal(total);

        Sales updatedSale = saleRepository.save(sale);

        return listToResponseDTO(updatedSale);
    }

    
    /**
     * Elimina una venta por su ID
     *
     * @param id El ID de la venta a eliminar
     * @return Mensaje confirmando que la venta fue eliminada
     */
    @Transactional
    public MessageResponseDTO deleteSale(Long id) {

        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        saleRepository.delete(sale);

        return new MessageResponseDTO("Venta eliminada correctamente con ID: " + id);
    }


}
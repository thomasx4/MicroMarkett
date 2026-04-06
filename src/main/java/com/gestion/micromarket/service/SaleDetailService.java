package com.gestion.micromarket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gestion.micromarket.dto.MessageResponseDTO;
import com.gestion.micromarket.dto.SaleDetailRequestDTO;
import com.gestion.micromarket.dto.SaleDetailResponseDTO;
import com.gestion.micromarket.entity.Products;
import com.gestion.micromarket.entity.SaleDetail;
import com.gestion.micromarket.entity.Sales;
import com.gestion.micromarket.repository.ProductsRepository;
import com.gestion.micromarket.repository.SaleDetailRepository;
import com.gestion.micromarket.repository.SaleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleDetailService {

    private final SaleDetailRepository saleDetailRepository;
    private final SaleRepository saleRepository;
    private final ProductsRepository productRepository;

    private static final BigDecimal IVA_RATE = new BigDecimal("0.19");

    // ------------------------------- CREATE -------------------------------
    @Transactional
    public MessageResponseDTO createSaleDetail(SaleDetailRequestDTO saleDetailRequestDTO) {

        Sales sale = saleRepository.findById(saleDetailRequestDTO.getSaleId())
                .orElseThrow(
                        () -> new RuntimeException("Venta no encontrada con ID: " + saleDetailRequestDTO.getSaleId()));

        Products product = productRepository.findById(saleDetailRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException(
                        "Producto no encontrado con ID: " + saleDetailRequestDTO.getProductId()));

        if (!product.getActive()) {
            throw new RuntimeException("El producto '" + product.getName() + "' no está activo, no se puede vender");
        }

        if (product.getStock() < saleDetailRequestDTO.getQuantity()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + product.getName() +
                    ". Stock disponible: " + product.getStock() + ", solicitado: "
                    + saleDetailRequestDTO.getQuantity());
        }

        BigDecimal subtotalDetalle = saleDetailRequestDTO.getUnitPrice()
                .multiply(BigDecimal.valueOf(saleDetailRequestDTO.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);

        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setSale(sale);
        saleDetail.setProduct(product);
        saleDetail.setQuantity(saleDetailRequestDTO.getQuantity());
        saleDetail.setUnitPrice(saleDetailRequestDTO.getUnitPrice());
        saleDetail.setSubtotal(subtotalDetalle);

        saleDetailRepository.save(saleDetail);

        product.setStock(product.getStock() - saleDetailRequestDTO.getQuantity());
        productRepository.save(product);

        updateSaleTotals(sale);

        return new MessageResponseDTO("Detalle de venta creado exitosamente con ID: " + saleDetail.getId());
    }

    // ------------------------------- GET ALL -------------------------------
    public List<SaleDetailResponseDTO> getAllSaleDetails() {
        List<SaleDetail> saleDetails = saleDetailRepository.findAll();
        List<SaleDetailResponseDTO> listSaleDetails = new ArrayList<>();

        for (SaleDetail saleDetail : saleDetails) {
            listSaleDetails.add(convertToResponseDTO(saleDetail));
        }

        return listSaleDetails;
    }

    // ------------------------------- GET BY ID -------------------------------
    public Optional<SaleDetailResponseDTO> getSaleDetailById(Long id) {
        SaleDetail saleDetail = saleDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado con ID: " + id));

        return Optional.of(convertToResponseDTO(saleDetail));
    }

    // ------------------------------- GET BY SALE ID
    // -------------------------------
    public List<SaleDetailResponseDTO> getSaleDetailsBySaleId(Long saleId) {
        // Validar que la venta existe
        saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + saleId));
        List<SaleDetail> saleDetails = saleDetailRepository.findBySaleId(saleId);

        if (saleDetails.isEmpty()) {
            throw new RuntimeException("No se encontraron detalles para la venta con ID: " + saleId);
        }

        List<SaleDetailResponseDTO> listSaleDetails = new ArrayList<>();
        for (SaleDetail saleDetail : saleDetails) {
            listSaleDetails.add(convertToResponseDTO(saleDetail));
        }

        return listSaleDetails;
    }

    // ------------------------------- GET BY PRODUCT ID
    // -------------------------------
    public List<SaleDetailResponseDTO> getSaleDetailsByProductId(Long productId) {

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        List<SaleDetail> saleDetails = saleDetailRepository.findByProductId(productId);

        if (saleDetails.isEmpty()) {
            throw new RuntimeException("No se encontraron ventas para el producto: " + product.getName());
        }

        List<SaleDetailResponseDTO> listSaleDetails = new ArrayList<>();
        for (SaleDetail saleDetail : saleDetails) {
            listSaleDetails.add(convertToResponseDTO(saleDetail));
        }

        return listSaleDetails;
    }

    // ------------------------------- UPDATE -------------------------------
    @Transactional
    public SaleDetailResponseDTO updateSaleDetail(Long id, SaleDetailRequestDTO saleDetailRequestDTO) {

        SaleDetail saleDetail = saleDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado con ID: " + id));

        Products oldProduct = saleDetail.getProduct();
        Products newProduct = null;

        if (saleDetailRequestDTO.getProductId() != null
                && !saleDetailRequestDTO.getProductId().equals(oldProduct.getId())) {
            newProduct = productRepository.findById(saleDetailRequestDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con ID: " + saleDetailRequestDTO.getProductId()));

            if (!newProduct.getActive()) {
                throw new RuntimeException("El producto '" + newProduct.getName() + "' no está activo");
            }
        }

        oldProduct.setStock(oldProduct.getStock() + saleDetail.getQuantity());
        productRepository.save(oldProduct);

        Integer finalQuantity = (saleDetailRequestDTO.getQuantity() != null) ? saleDetailRequestDTO.getQuantity()
                : saleDetail.getQuantity();
        BigDecimal finalUnitPrice = (saleDetailRequestDTO.getUnitPrice() != null) ? saleDetailRequestDTO.getUnitPrice()
                : saleDetail.getUnitPrice();

        Products productToUse = (newProduct != null) ? newProduct : oldProduct;
        if (productToUse.getStock() < finalQuantity) {
            throw new RuntimeException("Stock insuficiente para el producto: " + productToUse.getName() +
                    ". Stock disponible: " + productToUse.getStock());
        }

        BigDecimal newSubtotal = finalUnitPrice.multiply(BigDecimal.valueOf(finalQuantity))
                .setScale(2, RoundingMode.HALF_UP);

        if (newProduct != null) {
            saleDetail.setProduct(newProduct);
        }
        saleDetail.setQuantity(finalQuantity);
        saleDetail.setUnitPrice(finalUnitPrice);
        saleDetail.setSubtotal(newSubtotal);

        saleDetailRepository.save(saleDetail);

        productToUse.setStock(productToUse.getStock() - finalQuantity);
        productRepository.save(productToUse);

        updateSaleTotals(saleDetail.getSale());

        return convertToResponseDTO(saleDetail);
    }

    // ------------------------------- DELETE -------------------------------
    @Transactional
    public MessageResponseDTO deleteSaleDetailById(Long id) {

        SaleDetail saleDetail = saleDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado con ID: " + id));

        Sales sale = saleDetail.getSale();
        Products product = saleDetail.getProduct();

        product.setStock(product.getStock() + saleDetail.getQuantity());
        productRepository.save(product);

        saleDetailRepository.delete(saleDetail);

        updateSaleTotals(sale);

        return new MessageResponseDTO("Detalle de venta eliminado correctamente 😀");
    }

    // ------------------------------- DELETE ALL BY SALE ID
    // -------------------------------
    @Transactional
    public MessageResponseDTO deleteSaleDetailsBySaleId(Long saleId) {

        Sales sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + saleId));

        List<SaleDetail> saleDetails = saleDetailRepository.findBySaleId(saleId);

        if (saleDetails.isEmpty()) {
            throw new RuntimeException("No hay detalles para eliminar en la venta con ID: " + saleId);
        }

        for (SaleDetail saleDetail : saleDetails) {
            Products product = saleDetail.getProduct();
            product.setStock(product.getStock() + saleDetail.getQuantity());
            productRepository.save(product);
        }

        saleDetailRepository.deleteAll(saleDetails);

        sale.setSubtotal(BigDecimal.ZERO);
        sale.setVat(BigDecimal.ZERO);
        sale.setTotal(BigDecimal.ZERO);
        saleRepository.save(sale);

        return new MessageResponseDTO("Todos los detalles de la venta " + saleId + " fueron eliminados correctamente");
    }

    private void updateSaleTotals(Sales sale) {
        List<SaleDetail> details = saleDetailRepository.findBySaleId(sale.getId());

        BigDecimal newSubtotal = BigDecimal.ZERO;
        for (SaleDetail detail : details) {
            newSubtotal = newSubtotal.add(detail.getSubtotal());
        }

        BigDecimal newVat = newSubtotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal newTotal = newSubtotal.add(newVat).setScale(2, RoundingMode.HALF_UP);

        sale.setSubtotal(newSubtotal);
        sale.setVat(newVat);
        sale.setTotal(newTotal);
        saleRepository.save(sale);
    }

    private SaleDetailResponseDTO convertToResponseDTO(SaleDetail saleDetail) {
        SaleDetailResponseDTO response = new SaleDetailResponseDTO();
        response.setId(saleDetail.getId());
        response.setSaleId(saleDetail.getSale().getId());
        response.setProductId(saleDetail.getProduct().getId());
        response.setProductName(saleDetail.getProduct().getName());
        response.setProductBarcode(saleDetail.getProduct().getBarcode());
        response.setQuantity(saleDetail.getQuantity());
        response.setUnitPrice(saleDetail.getUnitPrice());
        response.setSubtotal(saleDetail.getSubtotal());
        return response;
    }
}
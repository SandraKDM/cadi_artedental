package com.cadi.artedental.budget.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.budget.catalog.entity.PriceCatalog;
import com.cadi.artedental.budget.catalog.repository.PriceCatalogRepository;
import com.cadi.artedental.budget.dto.BudgetItemRequest;
import com.cadi.artedental.budget.dto.BudgetItemResponse;
import com.cadi.artedental.budget.dto.BudgetResponse;
import com.cadi.artedental.budget.dto.CreateBudgetRequest;
import com.cadi.artedental.budget.dto.UpdateBudgetRequest;
import com.cadi.artedental.budget.entity.Budget;
import com.cadi.artedental.budget.entity.BudgetItem;
import com.cadi.artedental.budget.entity.BudgetStatus;
import com.cadi.artedental.budget.repository.BudgetRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BudgetService {

        private static final BigDecimal ZERO = BigDecimal.ZERO;

        private final BudgetRepository budgetRepository;

        private final PriceCatalogRepository priceCatalogRepository;

        public BudgetService(
                        BudgetRepository budgetRepository,
                        PriceCatalogRepository priceCatalogRepository) {
                this.budgetRepository = budgetRepository;
                this.priceCatalogRepository = priceCatalogRepository;
        }

        // =========================================================
        // CREAR PRESUPUESTO
        // =========================================================

        public BudgetResponse create(
                        String patientId,
                        CreateBudgetRequest request) {

                validatePatientId(patientId);
                validateRequest(request);

                Budget budget = new Budget();

                budget.setPatientId(
                                patientId.trim());

                budget.setBudgetNumber(
                                generateBudgetNumber());

                budget.setStatus(
                                BudgetStatus.DRAFT);

                budget.setNotes(request.notes() == null
                                ? null
                                : request.notes().trim());

                budget.setValidUntil(
                                request.validUntil());

                BigDecimal subtotal = ZERO;
                BigDecimal itemDiscounts = ZERO;

                for (BudgetItemRequest itemRequest : request.items()) {

                        BudgetItem item = createBudgetItem(
                                        itemRequest);

                        BigDecimal itemBase = item.getUnitPrice();

                        subtotal = subtotal.add(itemBase);



                        budget.addItem(item);
                }

                BigDecimal generalDiscount = request.discount() == null
                                ? ZERO
                                : request.discount();

                validateGeneralDiscount(
                                subtotal,
                                itemDiscounts,
                                generalDiscount);

                BigDecimal total = subtotal
                                .subtract(itemDiscounts)
                                .subtract(generalDiscount);

                budget.setSubtotal(subtotal);
                budget.setDiscount(generalDiscount);
                budget.setTotal(total);

                Budget saved = budgetRepository.save(budget);

                return toResponse(saved);
        }

        // =========================================================
        // CREAR ITEM
        // =========================================================

        private BudgetItem createBudgetItem(
                        BudgetItemRequest request) {

                PriceCatalog catalogItem = priceCatalogRepository
                                .findByIdAndActiveTrue(
                                                request.catalogItemId())
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "El tratamiento seleccionado "
                                                                                + "no existe o está inactivo: "
                                                                                + request.catalogItemId()));

                validateTooth(
                                catalogItem,
                                request.toothNumber());

                int quantity = request.quantity();

                BigDecimal unitPrice = catalogItem.getPrice();

                BigDecimal itemBase = unitPrice.multiply(
                                BigDecimal.valueOf(quantity));

                BigDecimal discount = request.discount() == null
                                ? ZERO
                                : request.discount();

                if (discount.compareTo(ZERO) < 0) {
                        throw new IllegalArgumentException(
                                        "El descuento del tratamiento "
                                                        + "no puede ser negativo");
                }

                if (discount.compareTo(itemBase) > 0) {
                        throw new IllegalArgumentException(
                                        "El descuento del tratamiento "
                                                        + catalogItem.getName()
                                                        + " no puede ser mayor "
                                                        + "al importe del tratamiento");
                }

                BigDecimal itemTotal = itemBase.subtract(discount);

                BudgetItem item = new BudgetItem();

                item.setCatalogItem(
                                catalogItem);

                item.setDescription(
                                catalogItem.getName());

                item.setToothNumber(
                                normalizeNullable(
                                                request.toothNumber()));

                // Snapshot del precio actual.
                item.setUnitPrice(unitPrice);

                item.setDescription(request.description());

                return item;
        }

        // =========================================================
        // VALIDAR DIENTE
        // =========================================================

        private void validateTooth(
                        PriceCatalog catalogItem,
                        String toothNumber) {

                if (Boolean.TRUE.equals(
                                catalogItem.getRequiresTooth())
                                &&
                                (toothNumber == null
                                                ||
                                                toothNumber.trim().isEmpty())) {

                        throw new IllegalArgumentException(
                                        "El tratamiento "
                                                        + catalogItem.getName()
                                                        + " requiere seleccionar un diente");
                }
        }

        // =========================================================
        // VALIDAR REQUEST
        // =========================================================

        private void validateRequest(
                        CreateBudgetRequest request) {

                if (request.items() == null
                                ||
                                request.items().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "El presupuesto debe contener "
                                                        + "al menos un tratamiento");
                }

                if (request.validUntil() != null
                                &&
                                request.validUntil()
                                                .isBefore(LocalDate.now())) {
                        throw new IllegalArgumentException(
                                        "La fecha de vigencia "
                                                        + "no puede estar en el pasado");
                }

                if (request.discount() != null
                                &&
                                request.discount()
                                                .compareTo(ZERO) < 0) {
                        throw new IllegalArgumentException(
                                        "El descuento general "
                                                        + "no puede ser negativo");
                }
        }

        // =========================================================
        // VALIDAR DESCUENTO GENERAL
        // =========================================================

        private void validateGeneralDiscount(
                        BigDecimal subtotal,
                        BigDecimal itemDiscounts,
                        BigDecimal generalDiscount) {

                BigDecimal available = subtotal.subtract(
                                itemDiscounts);

                if (generalDiscount
                                .compareTo(available) > 0) {
                        throw new IllegalArgumentException(
                                        "El descuento general "
                                                        + "no puede ser mayor a "
                                                        + "la cantidad restante "
                                                        + "del presupuesto");
                }
        }

        // =========================================================
        // VALIDAR PATIENT ID
        // =========================================================

        private void validatePatientId(
                        String patientId) {

                if (patientId == null
                                ||
                                patientId.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "El paciente es obligatorio");
                }
        }

        // =========================================================
        // GENERAR FOLIO
        // =========================================================

        private String generateBudgetNumber() {

                long nextNumber = budgetRepository.count() + 1;

                String number;

                do {

                        number = String.format(
                                        "P-%04d",
                                        nextNumber);

                        nextNumber++;

                } while (budgetRepository
                                .existsByBudgetNumber(number));

                return number;
        }

        // =========================================================
        // CONVERTIR A RESPONSE
        // =========================================================

        private BudgetResponse toResponse(
                        Budget budget) {

                List<BudgetItemResponse> items = budget.getItems()
                                .stream()
                                .map(this::toItemResponse)
                                .toList();

                return new BudgetResponse(
                                budget.getId(),
                                budget.getPatientId(),
                                budget.getBudgetNumber(),
                                budget.getStatus(),
                                budget.getSubtotal(),
                                budget.getDiscount(),
                                budget.getTotal(),
                                budget.getNotes(),
                                budget.getValidUntil(),
                                items,
                                budget.getCreatedAt(),
                                budget.getUpdatedAt());
        }

        private BudgetItemResponse toItemResponse(
                        BudgetItem item) {

                PriceCatalog catalogItem = item.getCatalogItem();

                return new BudgetItemResponse(
                                item.getId(),

                                catalogItem != null
                                                ? catalogItem.getId()
                                                : null,

                                catalogItem != null
                                                ? catalogItem.getCode()
                                                : null,
                                
                                catalogItem != null
                                                ? catalogItem.getName()
                                                : null,
                                item.getDescription(),
                                item.getToothNumber(),
                                item.getUnitPrice()
                                );    
        }

        // =========================================================
        // NORMALIZAR TEXTO
        // =========================================================

        private String normalizeNullable(
                        String value) {

                if (value == null) {
                        return null;
                }

                String normalized = value.trim();

                return normalized.isEmpty()
                                ? null
                                : normalized;
        }

        // =========================================================
        // OBTENER PRESUPUESTOS DEL PACIENTE
        // =========================================================

        @Transactional(readOnly = true)
        public List<BudgetResponse> findByPatientId(
                        String patientId) {

                validatePatientId(patientId);

                return budgetRepository
                                .findByPatientIdOrderByCreatedAtDesc(
                                                patientId.trim())
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // OBTENER PRESUPUESTO POR ID
        // =========================================================

        @Transactional(readOnly = true)
        public BudgetResponse findById(
                        String patientId,
                        UUID budgetId) {

                validatePatientId(patientId);

                Budget budget = budgetRepository
                                .findByIdAndPatientId(
                                                budgetId,
                                                patientId.trim())
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "Presupuesto no encontrado"));

                return toResponse(budget);
        }

        @Transactional
        public BudgetResponse update(
                        String patientId,
                        UUID budgetId,
                        UpdateBudgetRequest request) {

                Budget budget = budgetRepository
                                .findByIdAndPatientId(budgetId, patientId)
                                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado."));

                validateEditableStatus(budget);

                budget.getItems().clear();

                BigDecimal subtotal = BigDecimal.ZERO;
                BigDecimal itemDiscounts = BigDecimal.ZERO;

                for (BudgetItemRequest itemRequest : request.items()) {

                        BudgetItem item = createBudgetItem(itemRequest);

                        BigDecimal itemBase = item.getUnitPrice();
                                        
                        subtotal = subtotal.add(itemBase);

                        budget.addItem(item);
                }

                BigDecimal generalDiscount = request.discount() == null
                                ? BigDecimal.ZERO
                                : request.discount();

                validateGeneralDiscount(
                                subtotal,
                                itemDiscounts,
                                generalDiscount);

                budget.setSubtotal(subtotal);
                budget.setDiscount(generalDiscount);
                budget.setTotal(
                                subtotal
                                                .subtract(itemDiscounts)
                                                .subtract(generalDiscount));

                budget.setNotes(request.notes());
                budget.setValidUntil(request.validUntil());

                Budget updated = budgetRepository.save(budget);

                return toResponse(updated);
        }

        @Transactional
        public BudgetResponse updateStatus(
                        String patientId,
                        UUID budgetId,
                        BudgetStatus newStatus) {

                Budget budget = budgetRepository
                                .findByIdAndPatientId(budgetId, patientId)
                                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado."));

                validateStatusTransition(
                                budget.getStatus(),
                                newStatus);

                budget.setStatus(newStatus);

                Budget updated = budgetRepository.save(budget);

                return toResponse(updated);
        }

        private void validateEditableStatus(
                        Budget budget) {

                if (budget.getStatus() == BudgetStatus.CONVERTED) {
                        throw new IllegalArgumentException(
                                        "El presupuesto ya fue convertido a tratamientos.");
                }

                if (budget.getStatus() == BudgetStatus.CANCELLED) {
                        throw new IllegalArgumentException(
                                        "El presupuesto fue cancelado.");
                }

                if (budget.getStatus() == BudgetStatus.EXPIRED) {
                        throw new IllegalArgumentException(
                                        "El presupuesto ya expiró.");
                }
        }

        private void validateStatusTransition(
                        BudgetStatus current,
                        BudgetStatus next) {

                if (current == next) {
                        return;
                }

                if (current == BudgetStatus.CONVERTED) {
                        throw new IllegalArgumentException(
                                        "No se puede modificar un presupuesto convertido.");
                }

                if (current == BudgetStatus.CANCELLED) {
                        throw new IllegalArgumentException(
                                        "No se puede modificar un presupuesto cancelado.");
                }

                if (current == BudgetStatus.EXPIRED
                                && next != BudgetStatus.APPROVED) {
                        throw new IllegalArgumentException(
                                        "Un presupuesto expirado solo puede aprobarse nuevamente.");
                }
        }
}
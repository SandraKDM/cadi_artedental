package com.cadi.artedental.budget.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.cadi.artedental.budget.catalog.entity.PriceCatalog;

@Entity
@Table(name = "budget_items", indexes = {
        @Index(name = "idx_budget_items_budget_id", columnList = "budget_id"),
        @Index(name = "idx_budget_items_catalog_item_id", columnList = "catalog_item_id")
})
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_item_id")
    private PriceCatalog catalogItem;

    @Column(name = "tooth_number", length = 10)
    private String toothNumber;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "treatment_name", nullable = false, length = 200)
    private String treatmentName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public PriceCatalog getCatalogItem() {
        return catalogItem;
    }

    public void setCatalogItem(PriceCatalog catalogItem) {
        this.catalogItem = catalogItem;
    }

    public String getToothNumber() {
        return toothNumber;
    }

    public void setToothNumber(String toothNumber) {
        this.toothNumber = toothNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }
}
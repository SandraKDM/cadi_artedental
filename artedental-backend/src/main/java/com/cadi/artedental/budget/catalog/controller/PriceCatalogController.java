package com.cadi.artedental.budget.catalog.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cadi.artedental.budget.catalog.dto.CreatePriceCatalogRequest;
import com.cadi.artedental.budget.catalog.dto.PriceCatalogResponse;
import com.cadi.artedental.budget.catalog.dto.UpdatePriceCatalogRequest;
import com.cadi.artedental.budget.catalog.service.PriceCatalogService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/price-catalog")
public class PriceCatalogController {

    private final PriceCatalogService service;

    public PriceCatalogController(
            PriceCatalogService service) {
        this.service = service;
    }

    // =========================================================
    // CREAR
    // =========================================================

    @PostMapping
    public ResponseEntity<PriceCatalogResponse> create(
            @Valid @RequestBody CreatePriceCatalogRequest request) {

        PriceCatalogResponse response = service.create(request);

        System.out.println("ENTITY RESPONSE" + response.toString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        
    }

    // =========================================================
    // LISTAR CATÁLOGO ACTIVO
    // =========================================================

    @GetMapping
    public ResponseEntity<List<PriceCatalogResponse>> findAll() {

        List<PriceCatalogResponse> response = service.findAllActive();

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // LISTAR TODO EL CATÁLOGO
    // =========================================================

    @GetMapping("/all")
    public ResponseEntity<List<PriceCatalogResponse>> findAllCatalog() {

        List<PriceCatalogResponse> response = service.findAllCatalog();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // OBTENER POR ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<PriceCatalogResponse> findById(
            @PathVariable UUID id) {

        PriceCatalogResponse response = service.findById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // FILTRAR POR CATEGORÍA
    // =========================================================

    @GetMapping("/category/{category}")
    public ResponseEntity<List<PriceCatalogResponse>> findByCategory(
            @PathVariable String category) {

        List<PriceCatalogResponse> response = service.findByCategory(category);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ACTUALIZAR
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<PriceCatalogResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePriceCatalogRequest request) {

        PriceCatalogResponse response = service.update(id, request);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DESACTIVAR
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable UUID id) {

        service.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // REACTIVAR
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PriceCatalogResponse> activate(
            @PathVariable UUID id) {

        PriceCatalogResponse response = service.activate(id);

        return ResponseEntity.ok(response);
    }
}

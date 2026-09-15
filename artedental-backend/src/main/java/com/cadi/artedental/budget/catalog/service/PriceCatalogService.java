package com.cadi.artedental.budget.catalog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.budget.catalog.dto.CreatePriceCatalogRequest;
import com.cadi.artedental.budget.catalog.dto.PriceCatalogResponse;
import com.cadi.artedental.budget.catalog.dto.UpdatePriceCatalogRequest;
import com.cadi.artedental.budget.catalog.entity.PriceCatalog;
import com.cadi.artedental.budget.catalog.repository.PriceCatalogRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PriceCatalogService {

        private final PriceCatalogRepository repository;

        public PriceCatalogService(
                        PriceCatalogRepository repository) {
                this.repository = repository;
        }

        // =========================================================
        // CREAR
        // =========================================================

        public PriceCatalogResponse create(
                        CreatePriceCatalogRequest request) {

                System.out.println("========== CREATING CATALOG ITEM ==========");
                System.out.println("Request: " + request.toString());

                PriceCatalog entity = new PriceCatalog();

                if (request.code() == null || request.code().isBlank()) {
                        entity.setCode("000");
                } else {
                        entity.setCode(request.code());
                }

                entity.setName(
                                normalizeRequiredText(request.name()));
                entity.setDescription(
                                normalizeNullableText(request.description()));
                entity.setCategory(
                                normalizeCategory(request.category()));
                entity.setPrice(request.price());

                entity.setRequiresTooth(false);

                entity.setActive(true);

                entity.setcreatedAt(LocalDateTime.now());

                entity.setupdatedAt(LocalDateTime.now());

                PriceCatalog saved = repository.save(entity);

                return toResponse(saved);
        }

        // =========================================================
        // OBTENER CATÁLOGO ACTIVO
        // =========================================================

        @Transactional(readOnly = true)
        public List<PriceCatalogResponse> findAllActive() {

                return repository
                                .findByActiveTrueOrderByNameAsc()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // OBTENER TODO EL CATÁLOGO 
        // =========================================================

        @Transactional(readOnly = true)
        public List<PriceCatalogResponse> findAllCatalog() {

                return repository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // OBTENER POR ID
        // =========================================================

        @Transactional(readOnly = true)
        public PriceCatalogResponse findById(
                        UUID id) {
                return toResponse(
                                findEntityById(id));
        }

        // =========================================================
        // OBTENER POR CATEGORÍA
        // =========================================================

        @Transactional(readOnly = true)
        public List<PriceCatalogResponse> findByCategory(
                        String category) {

                final String normalizedCategory = normalizeCategory(category);

                return repository
                                .findByCategoryAndActiveTrueOrderByNameAsc(
                                                normalizedCategory)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // ACTUALIZAR
        // =========================================================

        public PriceCatalogResponse update(
                        UUID id,
                        UpdatePriceCatalogRequest request) {

                PriceCatalog entity = findEntityById(id);

                entity.setName(
                                normalizeRequiredText(request.name()));

                entity.setDescription(
                                normalizeNullableText(
                                                request.description()));

                entity.setCategory(
                                normalizeCategory(
                                                request.category()));

                entity.setPrice(
                                request.price());

                entity.setRequiresTooth(false);

                entity.setActive(
                                request.active());

                PriceCatalog updated = repository.save(entity);

                return toResponse(updated);
        }

        // =========================================================
        // DESACTIVAR
        // =========================================================

        public void deactivate(
                        UUID id) {

                PriceCatalog entity = findEntityById(id);

                entity.setActive(false);

                repository.save(entity);
        }

        // =========================================================
        // ACTIVAR
        // =========================================================

        public PriceCatalogResponse activate(
                        UUID id) {

                PriceCatalog entity = findEntityById(id);

                entity.setActive(true);

                PriceCatalog updated = repository.save(entity);

                return toResponse(updated);
        }

        // =========================================================
        // MÉTODOS INTERNOS
        // =========================================================

        private PriceCatalog findEntityById(
                        UUID id) {

                return repository
                                .findById(id)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "Elemento del catálogo no encontrado: "
                                                                                + id));
        }

        private PriceCatalogResponse toResponse(PriceCatalog entity) {

                return new PriceCatalogResponse(
                                entity.getId(),
                                entity.getCode(),
                                entity.getName(),
                                entity.getDescription(),
                                entity.getCategory(),
                                entity.getPrice(),
                                entity.getRequiresTooth(),
                                entity.getActive(),
                                entity.getCreatedAt(),
                                entity.getUpdatedAt());
        }

        // =========================================================
        // NORMALIZACIÓN
        // =========================================================

        private String normalizeCode(
                        String value) {

                return value
                                .trim()
                                .toUpperCase();
        }

        private String normalizeCategory(
                        String value) {

                return value
                                .trim()
                                .toUpperCase();
        }

        private String normalizeRequiredText(
                        String value) {

                return value.trim();
        }

        private String normalizeNullableText(
                        String value) {

                if (value == null) {
                        return null;
                }

                final String normalized = value.trim();

                return normalized.isEmpty()
                                ? null
                                : normalized;
        }
}
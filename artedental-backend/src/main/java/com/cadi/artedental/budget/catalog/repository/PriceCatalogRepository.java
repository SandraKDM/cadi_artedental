package com.cadi.artedental.budget.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadi.artedental.budget.catalog.entity.PriceCatalog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PriceCatalogRepository
        extends JpaRepository<PriceCatalog, UUID> {

    List<PriceCatalog> findAll();

    List<PriceCatalog> findByActiveTrueOrderByNameAsc();

    List<PriceCatalog> findByCategoryAndActiveTrueOrderByNameAsc(
        String category
    );

    Optional<PriceCatalog> findByCode(String code);

    Optional<PriceCatalog> findByIdAndActiveTrue(UUID id);

    boolean existsByCode(String code);
}
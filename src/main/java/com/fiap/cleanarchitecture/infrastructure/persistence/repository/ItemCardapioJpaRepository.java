package com.fiap.cleanarchitecture.infrastructure.persistence.repository;

import com.fiap.cleanarchitecture.infrastructure.persistence.entities.ItemCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapioEntity, Long> {
    Optional<ItemCardapioEntity> findByIdAndRestauranteId(Long id, Long restauranteId);
    List<ItemCardapioEntity> findByRestauranteIdOrderByIdAsc(Long restauranteId);
}

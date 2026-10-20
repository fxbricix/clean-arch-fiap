package com.fiap.cleanarchitecture.infrastructure.persistence.repository;

import com.fiap.cleanarchitecture.infrastructure.persistence.entities.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteJpaRepository extends JpaRepository<RestauranteEntity, Long> {
    boolean existsByDonoId(Long donoId);
}

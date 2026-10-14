package com.fiap.cleanarchitecture.infrastructure.persistence.repository;

import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoUsuarioJpaRepository extends JpaRepository<TipoUsuarioEntity, Long> {
    boolean existsById(Long id);
    boolean existsByNome(String nome);
}

package com.fiap.cleanarchitecture.infrastructure.persistence.repository;

import com.fiap.cleanarchitecture.infrastructure.persistence.entities.UsuarioEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "tipoUsuario")
    List<UsuarioEntity> findAll(Sort sort);
}

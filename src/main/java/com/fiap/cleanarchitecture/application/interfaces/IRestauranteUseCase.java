package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;

import java.util.List;
import java.util.Optional;

public interface IRestauranteUseCase {
    Restaurante criar(Restaurante restaurante);
    Optional<Restaurante> buscarPorId(Long id);
    List<Restaurante> listar();
    Restaurante atualizar(Long id, Restaurante restaurante);
    void excluir(Long id);
}

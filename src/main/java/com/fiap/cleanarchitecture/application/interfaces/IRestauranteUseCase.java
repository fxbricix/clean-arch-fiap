package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;

import java.util.List;

public interface IRestauranteUseCase {
    Restaurante criar(Restaurante restaurante);
    Restaurante buscarPorId(Long id);
    List<Restaurante> listar();
    Restaurante atualizar(Long id, Restaurante restaurante);
    void excluir(Long id);
}

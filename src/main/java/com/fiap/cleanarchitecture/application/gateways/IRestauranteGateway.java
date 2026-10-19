package com.fiap.cleanarchitecture.application.gateways;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;

import java.util.List;
import java.util.Optional;

public interface IRestauranteGateway {
    Restaurante salvar(Restaurante restaurante);
    Optional<Restaurante> buscarPorId(Long id);
    List<Restaurante> listar();
    void excluir(Long id);
    boolean existePorDonoId(Long donoId);
}

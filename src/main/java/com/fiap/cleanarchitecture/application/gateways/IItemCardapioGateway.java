package com.fiap.cleanarchitecture.application.gateways;

import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;

import java.util.List;
import java.util.Optional;

public interface IItemCardapioGateway {
    ItemCardapio salvar(ItemCardapio item);
    Optional<ItemCardapio> buscarPorIdERestauranteId(Long id, Long restauranteId);
    List<ItemCardapio> listarPorRestauranteId(Long restauranteId);
    void excluir(Long id);
}

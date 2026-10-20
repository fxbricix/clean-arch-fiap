package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;

import java.math.BigDecimal;
import java.util.List;

public interface IItemCardapioUseCase {
    ItemCardapio criar(Long restauranteId,
                       Long usuarioId,
                       String nome,
                       String descricao,
                       BigDecimal preco,
                       boolean apenasNoLocal,
                       String caminhoFoto);

    ItemCardapio buscarPorId(Long restauranteId, Long id);
    List<ItemCardapio> listar(Long restauranteId);

    ItemCardapio atualizar(Long restauranteId,
                           Long id, Long usuarioId,
                           String nome, String descricao,
                           BigDecimal preco,
                           boolean apenasNoLocal,
                           String caminhoFoto);

    void excluir(Long restauranteId, Long id, Long usuarioId);
}

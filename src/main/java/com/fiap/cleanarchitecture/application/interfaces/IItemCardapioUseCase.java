package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.application.exceptions.AcessoNegadoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;

import java.math.BigDecimal;
import java.util.List;

/**
 * Casos de uso de ItemCardapio.
 */
public interface IItemCardapioUseCase {
    /**
     * Cria um item no cardápio de um restaurante.
     *
     * @param restauranteId id do restaurante
     * @param usuarioId     id do usuário solicitante (deve ser o dono do restaurante)
     * @return o item criado
     * @throws RecursoNaoEncontradoException se restaurante ou usuário não existir
     * @throws AcessoNegadoException se o usuário não for o dono do restaurante
     */
    ItemCardapio criar(Long restauranteId,
                       Long usuarioId,
                       String nome,
                       String descricao,
                       BigDecimal preco,
                       boolean apenasNoLocal,
                       String caminhoFoto);

    /**
     * Busca um item de um restaurante.
     *
     * @throws RecursoNaoEncontradoException se o restaurante ou o item não existir
     */
    ItemCardapio buscarPorId(Long restauranteId, Long id);
    /**
     * Lista os itens do cardápio de um restaurante.
     *
     * @throws RecursoNaoEncontradoException se o restaurante não existir
     */
    List<ItemCardapio> listar(Long restauranteId);

    /**
     * Atualiza um item do cardápio. Apenas o dono do restaurante pode fazê-lo.
     *
     * @throws RecursoNaoEncontradoException se restaurante, item ou usuário não existir
     * @throws AcessoNegadoException se o usuário não for o dono do restaurante
     */
    ItemCardapio atualizar(Long restauranteId,
                           Long id, Long usuarioId,
                           String nome, String descricao,
                           BigDecimal preco,
                           boolean apenasNoLocal,
                           String caminhoFoto);

    /**
     * Exclui um item do cardápio. Apenas o dono do restaurante pode fazê-lo.
     *
     * @throws RecursoNaoEncontradoException se restaurante, item ou usuário não existir
     * @throws AcessoNegadoException se o usuário não for o dono do restaurante
     */
    void excluir(Long restauranteId, Long id, Long usuarioId);
}

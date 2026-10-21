package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.exceptions.RegraDeNegocioException;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;

import java.util.List;

/**
 * Casos de uso de Restaurante.
 */
public interface IRestauranteUseCase {
    /**
     * Cria um restaurante.
     *
     * @throws RegraDeNegocioException se não houver dono ou o usuário não for dono de restaurante
     * @throws RecursoNaoEncontradoException se o dono não existir
     */
    Restaurante criar(Restaurante restaurante);
    /**
     * Busca um restaurante pelo id.
     *
     * @throws RecursoNaoEncontradoException se não existir
     */
    Restaurante buscarPorId(Long id);
    /**
     * Lista todos os restaurantes.
     */
    List<Restaurante> listar();
    /**
     * Atualiza os dados de um restaurante.
     *
     * @throws RecursoNaoEncontradoException se o restaurante ou o dono não existir
     * @throws RegraDeNegocioException se o usuário não for dono de restaurante
     */
    Restaurante atualizar(Long id, Restaurante restaurante);
    /**
     * Exclui um restaurante.
     *
     * @throws RecursoNaoEncontradoException se não existir
     */
    void excluir(Long id);
}

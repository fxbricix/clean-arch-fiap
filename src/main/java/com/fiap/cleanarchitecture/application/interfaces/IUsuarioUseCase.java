package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.exceptions.RegraDeNegocioException;
import com.fiap.cleanarchitecture.domain.entities.Usuario;

import java.util.List;

/**
 * Casos de uso de Usuario.
 */
public interface IUsuarioUseCase {
    /**
     * Cria um usuário.
     *
     * @throws ConflitoException se o e-mail já estiver cadastrado
     * @throws RecursoNaoEncontradoException se o tipo de usuário não existir
     */
    Usuario criar(String nome, String email, Long tipoUsuarioId);
    /**
     * Busca um usuário pelo id.
     *
     * @throws RecursoNaoEncontradoException se não existir
     */
    Usuario buscarPorId(Long id);
    /**
     * Lista todos os usuários.
     */
    List<Usuario> listar();
    /**
     * Associa um novo tipo a um usuário.
     *
     * @throws RecursoNaoEncontradoException se usuário ou tipo não existir
     * @throws RegraDeNegocioException se o usuário possuir restaurantes e deixaria de ser dono
     */
    Usuario associarTipo(Long usuarioId, Long tipoUsuarioId);
}

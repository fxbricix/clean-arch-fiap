package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

import java.util.List;

/**
 * Casos de uso de TipoUsuario.
 */
public interface ITipoUsuarioUseCase {
    /**
     * Cria um tipo de usuário.
     *
     * @throws ConflitoException se já existir um tipo com o mesmo nome
     */
    TipoUsuario criar(TipoUsuario tipoUsuario);
    /**
     * Busca um tipo de usuário pelo id.
     *
     * @throws RecursoNaoEncontradoException se não existir
     */
    TipoUsuario buscarPorId(Long id);
    /**
     * Lista todos os tipos de usuário.
     */
    List<TipoUsuario> listar();
    /**
     * Altera o nome de um tipo de usuário.
     *
     * @throws RecursoNaoEncontradoException se não existir
     * @throws ConflitoException se o tipo for sistêmico ou o novo nome já estiver em uso
     */
    TipoUsuario alterar(Long id, String novoNome);
    /**
     * Exclui um tipo de usuário.
     *
     * @throws RecursoNaoEncontradoException se não existir
     * @throws ConflitoException se o tipo for sistêmico
     */
    void deletar(Long id);
}

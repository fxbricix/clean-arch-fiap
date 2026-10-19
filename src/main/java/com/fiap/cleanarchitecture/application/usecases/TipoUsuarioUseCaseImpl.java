package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.ITipoUsuarioUseCase;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

import java.util.List;

public class TipoUsuarioUseCaseImpl implements ITipoUsuarioUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    public TipoUsuarioUseCaseImpl(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public TipoUsuario criar(TipoUsuario tipoUsuario) {
        if (tipoUsuarioGateway.existePorNome(tipoUsuario.getNome())) {
            throw new ConflitoException("Tipo de usuário já existe com o nome: " + tipoUsuario.getNome());
        }
        return tipoUsuarioGateway.salvar(tipoUsuario);
    }

    @Override
    public void deletar(Long id) {
        TipoUsuario tipo = buscarPorId(id);
        if (tipo.ehTipoSistemico()) {
            throw new ConflitoException("Não é permitido excluir tipos de usuário sistêmicos.");
        }
        tipoUsuarioGateway.excluir(id);
    }

    @Override
    public TipoUsuario alterar(Long id, String novoNome) {
        TipoUsuario tipo = buscarPorId(id);
        if (tipo.ehTipoSistemico()) {
            throw new ConflitoException("Não é permitido alterar o nome de tipos de usuário sistêmicos.");
        }
        TipoUsuario novoTipoUsuario = new TipoUsuario(novoNome);
        if (novoTipoUsuario.ehTipoSistemico()) {
            throw new ConflitoException("Não é permitido alterar o nome de tipos de usuário sistêmicos.");
        }
        String nomeAnterior = tipo.getNome();
        if (!novoTipoUsuario.getNome().equals(nomeAnterior)
                && tipoUsuarioGateway.existePorNome(novoTipoUsuario.getNome())) {
            throw new ConflitoException("Tipo de usuário já existe com o nome: " + novoTipoUsuario.getNome());
        }
        tipo.alterarNome(novoTipoUsuario.getNome());
        return tipoUsuarioGateway.atualizar(tipo);
    }

    @Override
    public TipoUsuario buscarPorId(Long id) {
        return tipoUsuarioGateway.buscarPorId(id).orElseThrow(
            () -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado com o ID: " + id)
        );
    }

    @Override
    public List<TipoUsuario> listar() {
        return tipoUsuarioGateway.listar();
    }
}

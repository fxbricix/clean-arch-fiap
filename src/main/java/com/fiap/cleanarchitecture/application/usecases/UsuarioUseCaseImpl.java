package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.IUsuarioUseCase;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public class UsuarioUseCaseImpl implements IUsuarioUseCase {

    private IUsuarioGateway usuarioGateway;

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        return usuarioGateway.salvar(usuario);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return Optional.of(usuarioGateway.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id)));
    }

    @Override
    public Usuario atualizarTipoDoUsuario(Long id, TipoUsuario tipoAtualizado) {
        Usuario usuario = buscarUsuarioPorId(id).get();
        usuario.alterarTipo(tipoAtualizado);
        return usuarioGateway.salvar(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioGateway.listar();
    }
}

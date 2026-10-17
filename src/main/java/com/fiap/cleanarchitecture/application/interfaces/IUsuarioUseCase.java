package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioUseCase {
    Usuario criarUsuario(Usuario usuario);
    Optional<Usuario> buscarUsuarioPorId(Long id);
    Usuario atualizarTipoDoUsuario(Long id, TipoUsuario tipoAtualizado);
    List<Usuario> listarUsuarios();
}

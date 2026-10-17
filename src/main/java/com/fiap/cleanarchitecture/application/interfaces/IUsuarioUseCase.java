package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioUseCase {
    Usuario criar(String nome, String email, Long tipoUsuarioId);
    Usuario buscarPorId(Long id);
    List<Usuario> listar();
    Usuario associarTipo(Long usuarioId, Long tipoUsuarioId);
}

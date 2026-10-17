package com.fiap.cleanarchitecture.application.gateways;

import com.fiap.cleanarchitecture.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioGateway {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> listar();
}

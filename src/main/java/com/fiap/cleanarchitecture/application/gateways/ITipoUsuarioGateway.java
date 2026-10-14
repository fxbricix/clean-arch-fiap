package com.fiap.cleanarchitecture.application.gateways;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface ITipoUsuarioGateway {
    TipoUsuario salvar(TipoUsuario tipoUsuario);
    Optional<TipoUsuario> buscarPorId(Long id);
    List<TipoUsuario> listar();
    boolean existePorNome(String nome);
    void excluir(Long id);
    TipoUsuario atualizar(TipoUsuario tipoUsuario);
}
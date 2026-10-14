package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

import java.util.List;

public interface ITipoUsuarioUseCase {
    TipoUsuario criar(TipoUsuario tipoUsuario);
    TipoUsuario buscarPorId(Long id);
    List<TipoUsuario> listar();
    TipoUsuario alterar(Long id, String novoNome);
    void deletar(Long id);
}

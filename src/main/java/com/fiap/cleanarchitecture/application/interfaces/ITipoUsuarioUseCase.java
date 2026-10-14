package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

public interface ITipoUsuarioUseCase {
    TipoUsuario criar(TipoUsuario tipoUsuario);
    void deletar(Long id);
    TipoUsuario alterar(TipoUsuario tipoUsuario);
    TipoUsuario buscarPorId(Long id);
}

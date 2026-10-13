package com.fiap.cleanarchitecture.application.interfaces;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

public interface ITipoUsuarioUseCase {
    TipoUsuario criar(TipoUsuario tipoUsuario);
    void deletar(TipoUsuario tipoUsuario);
    TipoUsuario alterar(Long id);
    TipoUsuario buscarPorId(Long id);
}

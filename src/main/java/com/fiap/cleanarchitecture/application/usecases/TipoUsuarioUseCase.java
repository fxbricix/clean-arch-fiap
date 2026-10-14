package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.ITipoUsuarioUseCase;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;

public class TipoUsuarioUseCase implements ITipoUsuarioUseCase {

    private ITipoUsuarioGateway tipoUsuarioGateway;

    public TipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public TipoUsuario criar(TipoUsuario tipoUsuario) {
        return tipoUsuarioGateway.salvar(tipoUsuario);
    }

    @Override
    public void deletar(Long id) {
        tipoUsuarioGateway.excluir(id);
    }

    @Override
    public TipoUsuario alterar(TipoUsuario tipoUsuario) {
        return tipoUsuarioGateway.atualizar(tipoUsuario);
    }

    @Override
    public TipoUsuario buscarPorId(Long id) {
        return tipoUsuarioGateway.buscarPorId(id).orElse(null);
    }
}

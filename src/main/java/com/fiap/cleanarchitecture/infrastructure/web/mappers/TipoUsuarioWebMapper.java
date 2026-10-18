package com.fiap.cleanarchitecture.infrastructure.web.mappers;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarTipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.TipoUsuarioDTO;

public class TipoUsuarioWebMapper {

    public TipoUsuarioWebMapper() {
    }

    public TipoUsuario toDomainEntity(CriarTipoUsuarioDTO criarTipoUsuarioDTO) {
        if (criarTipoUsuarioDTO == null) {
            return null;
        }
        return new TipoUsuario(null, criarTipoUsuarioDTO.nome());
    }

    public TipoUsuario toDomainEntity(TipoUsuarioDTO tipoUsuarioDTO) {
        if (tipoUsuarioDTO == null) {
            return null;
        }
        return new TipoUsuario(tipoUsuarioDTO.id(), tipoUsuarioDTO.nome());
    }

    public TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return new TipoUsuarioDTO(tipoUsuario.getId(), tipoUsuario.getNome());
    }
}

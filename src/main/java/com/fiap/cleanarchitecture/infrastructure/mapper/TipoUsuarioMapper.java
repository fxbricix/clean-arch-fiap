package com.fiap.cleanarchitecture.infrastructure.mapper;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.infrastructure.controllers.dtos.CriarTipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.controllers.dtos.TipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;

public class TipoUsuarioMapper {
    public TipoUsuarioMapper() {
    }

    public TipoUsuario toDomainEntity(TipoUsuarioEntity tipoUsuarioEntity) {
        if (tipoUsuarioEntity == null) {
            return null;
        }
        TipoUsuario tipoUsuario = new TipoUsuario(tipoUsuarioEntity.getId(), tipoUsuarioEntity.getNome());
        return tipoUsuario;
    }

    public TipoUsuarioEntity toEntity(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        TipoUsuarioEntity tipoUsuarioEntity = new TipoUsuarioEntity();
        tipoUsuarioEntity.setId(tipoUsuario.getId());
        tipoUsuarioEntity.setNome(tipoUsuario.getNome());
        return tipoUsuarioEntity;
    }

    public TipoUsuario toDomainEntity(CriarTipoUsuarioDTO criarTipoUsuarioDTO) {
        if (criarTipoUsuarioDTO == null) {
            return null;
        }
        return new TipoUsuario(null, criarTipoUsuarioDTO.nome());
    }

    public TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return new TipoUsuarioDTO(tipoUsuario.getId(), tipoUsuario.getNome());
    }
}

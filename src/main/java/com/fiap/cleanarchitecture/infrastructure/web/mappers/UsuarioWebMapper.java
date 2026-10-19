package com.fiap.cleanarchitecture.infrastructure.web.mappers;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.TipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.UsuarioDTO;

public class UsuarioWebMapper {

    TipoUsuarioWebMapper tipoUsuarioWebMapper = new TipoUsuarioWebMapper();

    public UsuarioWebMapper() {
    }

    public UsuarioDTO toDTO(Usuario usuarioDomain){
        if (usuarioDomain == null) {
            return null;
        }

        TipoUsuarioDTO tipoUsuarioDTO = tipoUsuarioWebMapper.toDTO(usuarioDomain.getTipoUsuario());

        return new UsuarioDTO(
                usuarioDomain.getId(),
                usuarioDomain.getNome(),
                usuarioDomain.getEmail().valor(),
                tipoUsuarioDTO
        );
    }
}

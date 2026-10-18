package com.fiap.cleanarchitecture.infrastructure.persistence.mappers;

import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.UsuarioEntity;

public class UsuarioPersistenceMapper {

    private final TipoUsuarioPersistenceMapper tipoUsuarioMapper = new TipoUsuarioPersistenceMapper();

    public Usuario toDomainEntity(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                new Email(entity.getEmail()),
                tipoUsuarioMapper.toDomainEntity(entity.getTipoUsuario()));
    }

    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail().valor());
        entity.setTipoUsuario(tipoUsuarioMapper.toEntity(usuario.getTipoUsuario()));
        return entity;
    }
}

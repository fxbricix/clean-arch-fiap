package com.fiap.cleanarchitecture.infrastructure.web.dtos;

public record UsuarioDTO(
        Long id,
        String name,
        String email,
        TipoUsuarioDTO tipoUsuario
) {
}

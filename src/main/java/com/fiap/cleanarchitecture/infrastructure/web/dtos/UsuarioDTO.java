package com.fiap.cleanarchitecture.infrastructure.web.dtos;

public record UsuarioDTO(
        Long id,
        String nome,
        String email,
        TipoUsuarioDTO tipoUsuario
) {
}

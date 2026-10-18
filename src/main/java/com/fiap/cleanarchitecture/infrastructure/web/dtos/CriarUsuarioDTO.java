package com.fiap.cleanarchitecture.infrastructure.web.dtos;

public record CriarUsuarioDTO (
        String nome,
        String email,
        Long tipoUsuarioId
) {
}

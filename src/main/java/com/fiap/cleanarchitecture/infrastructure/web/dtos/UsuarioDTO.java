package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Usuário")
public record UsuarioDTO(
        @Schema(description = "Id do usuário", example = "1")
        Long id,
        @Schema(description = "Nome do usuário", example = "Maria Silva")
        String nome,
        @Schema(description = "E-mail do usuário", example = "maria@email.com")
        String email,
        @Schema(description = "Tipo do usuário")
        TipoUsuarioDTO tipoUsuario
) {
}

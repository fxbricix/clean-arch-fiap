package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de usuário")
public record TipoUsuarioDTO(
        @Schema(description = "Id do tipo de usuário", example = "1")
        Long id,
        @Schema(description = "Nome do tipo de usuário", example = "Cliente")
        String nome
) {
}

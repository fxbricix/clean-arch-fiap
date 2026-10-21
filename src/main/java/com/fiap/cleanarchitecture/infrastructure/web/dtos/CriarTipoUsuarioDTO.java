package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação ou alteração de um tipo de usuário")
public record CriarTipoUsuarioDTO(
    @NotBlank
    @Size(max = 50, message = "Nome do tipo de usuário deve ter no máximo 50 caracteres")
    @Schema(description = "Nome do tipo de usuário", example = "Cliente")
    String nome
) {
}

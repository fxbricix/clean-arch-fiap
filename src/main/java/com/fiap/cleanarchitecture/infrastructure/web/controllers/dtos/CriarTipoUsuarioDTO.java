package com.fiap.cleanarchitecture.infrastructure.web.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarTipoUsuarioDTO(
    @NotBlank
    @Size(max = 50, message = "Nome do tipo de usuário deve ter no máximo 50 caracteres")
    String nome
) {
}

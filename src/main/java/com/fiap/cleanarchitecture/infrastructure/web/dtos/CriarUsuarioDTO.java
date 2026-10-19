package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarUsuarioDTO (
        @NotBlank(message = "O nome do usuário é obrigatório")
        @Size(max = 120, message = "O nome do usuário deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório")
        @Email(message = "O email do usuário deve ser válido")
        @Size(max = 160, message = "O email do usuário deve ter no máximo 160 caracteres")
        String email,

        @NotNull(message = "O tipo de usuário é obrigatório")
        Long tipoUsuarioId
) {
}

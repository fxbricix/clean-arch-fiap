package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um usuário")
public record CriarUsuarioDTO (
        @NotBlank(message = "O nome do usuário é obrigatório")
        @Size(max = 120, message = "O nome do usuário deve ter no máximo 120 caracteres")
        @Schema(description = "Nome do usuário", example = "Maria Silva")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório")
        @Email(message = "O email do usuário deve ser válido")
        @Size(max = 160, message = "O email do usuário deve ter no máximo 160 caracteres")
        @Schema(description = "E-mail único do usuário", example = "maria@email.com")
        String email,

        @NotNull(message = "O tipo de usuário é obrigatório")
        @Schema(description = "Id do tipo de usuário", example = "1")
        Long tipoUsuarioId
) {
}

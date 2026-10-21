package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação ou atualização de um restaurante")
public record CriarRestauranteDTO(
        @NotBlank(message = "O nome do restaurante é obrigatório")
        @Size(max = 120, message = "O nome do restaurante deve ter no máximo 120 caracteres")
        @Schema(description = "Nome do restaurante", example = "Cantina do Zé")
        String nome,

        @NotBlank(message = "O endereço do restaurante é obrigatório")
        @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres")
        @Schema(description = "Endereço completo", example = "Rua das Flores, 123 - São Paulo/SP")
        String endereco,

        @NotBlank(message = "O tipo de cozinha é obrigatório")
        @Size(max = 60, message = "O tipo de cozinha deve ter no máximo 60 caracteres")
        @Schema(description = "Tipo de cozinha", example = "Italiana")
        String tipoCozinha,

        @NotNull(message = "O horário de funcionamento é obrigatório")
        @Valid
        @Schema(description = "Horário de funcionamento")
        HorarioFuncionamentoDTO horarioFuncionamento,

        @NotNull(message = "O dono do restaurante é obrigatório")
        @Schema(description = "Id do usuário dono do restaurante", example = "1")
        Long donoId
) {
}
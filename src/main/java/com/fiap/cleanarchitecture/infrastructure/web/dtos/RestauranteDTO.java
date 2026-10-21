package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Restaurante")
public record RestauranteDTO(
        @Schema(description = "Id do restaurante", example = "1")
        Long id,
        @Schema(description = "Nome do restaurante", example = "Cantina do Zé")
        String nome,
        @Schema(description = "Endereço completo", example = "Rua das Flores, 123 - São Paulo/SP")
        String endereco,
        @Schema(description = "Tipo de cozinha", example = "Italiana")
        String tipoCozinha,
        @Schema(description = "Horário de funcionamento")
        HorarioFuncionamentoDTO horarioFuncionamento,
        @Schema(description = "Id do dono do restaurante", example = "1")
        Long donoId
) {
}
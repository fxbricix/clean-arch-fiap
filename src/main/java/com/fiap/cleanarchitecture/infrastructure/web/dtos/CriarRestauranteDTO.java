package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarRestauranteDTO(
        @NotBlank(message = "O nome do restaurante é obrigatório")
        @Size(max = 120, message = "O nome do restaurante deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O endereço do restaurante é obrigatório")
        @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres")
        String endereco,

        @NotBlank(message = "O tipo de cozinha é obrigatório")
        @Size(max = 60, message = "O tipo de cozinha deve ter no máximo 60 caracteres")
        String tipoCozinha,

        @NotNull(message = "O horário de funcionamento é obrigatório")
        @Valid
        HorarioFuncionamentoDTO horarioFuncionamento,

        @NotNull(message = "O dono do restaurante é obrigatório")
        Long donoId
) {
}
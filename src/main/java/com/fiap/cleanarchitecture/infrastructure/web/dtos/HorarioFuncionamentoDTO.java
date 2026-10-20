package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioFuncionamentoDTO(
        @NotNull(message = "O horário de abertura é obrigatório")
        @JsonFormat(pattern = "HH:mm")
        LocalTime abertura,

        @NotNull(message = "O horário de fechamento é obrigatório")
        @JsonFormat(pattern = "HH:mm")
        LocalTime fechamento
) {
}
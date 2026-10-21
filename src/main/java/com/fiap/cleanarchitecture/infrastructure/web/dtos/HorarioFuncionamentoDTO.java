package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "Horário de funcionamento no formato HH:mm")
public record HorarioFuncionamentoDTO(
        @NotNull(message = "O horário de abertura é obrigatório")
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "Horário de abertura", example = "11:00")
        LocalTime abertura,

        @NotNull(message = "O horário de fechamento é obrigatório")
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "Horário de fechamento", example = "23:00")
        LocalTime fechamento
) {
}
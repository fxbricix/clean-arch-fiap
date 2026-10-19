package com.fiap.cleanarchitecture.domain.valueobjects;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;

import java.time.LocalTime;

public record HorarioFuncionamento(LocalTime abertura, LocalTime fechamento) {

    public HorarioFuncionamento {
        if (abertura == null || fechamento == null) {
            throw new DominioException("Horário de abertura e fechamento não podem ser nulos.");
        }

        if (!abertura.isBefore(fechamento)) {
            throw new DominioException("Horário de abertura deve ser antes do horário de fechamento.");
        }
    }
}

package com.fiap.cleanarchitecture.domain.valueobjects;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HorarioFuncionamentoTest {

    @Test
    @DisplayName("Deve criar horário de funcionamento quando a abertura antecede o fechamento")
    void deveCriarHorarioValido() {
        LocalTime abertura = LocalTime.of(9, 0);
        LocalTime fechamento = LocalTime.of(18, 0);

        HorarioFuncionamento horario = new HorarioFuncionamento(abertura, fechamento);

        assertEquals(abertura, horario.abertura());
        assertEquals(fechamento, horario.fechamento());
    }

    @Test
    @DisplayName("Não deve aceitar horário de abertura nulo")
    void naoDeveAceitarAberturaNula() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new HorarioFuncionamento(null, LocalTime.of(18, 0)));

        assertEquals("Horário de abertura e fechamento não podem ser nulos.", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar horário de fechamento nulo")
    void naoDeveAceitarFechamentoNulo() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new HorarioFuncionamento(LocalTime.of(9, 0), null));

        assertEquals("Horário de abertura e fechamento não podem ser nulos.", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar abertura igual ao fechamento")
    void naoDeveAceitarHorariosIguais() {
        LocalTime horario = LocalTime.of(9, 0);

        DominioException exception = assertThrows(DominioException.class,
                () -> new HorarioFuncionamento(horario, horario));

        assertEquals("Horário de abertura deve ser antes do horário de fechamento.", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar abertura posterior ao fechamento")
    void naoDeveAceitarAberturaPosteriorAoFechamento() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new HorarioFuncionamento(LocalTime.of(18, 0), LocalTime.of(9, 0)));

        assertEquals("Horário de abertura deve ser antes do horário de fechamento.", exception.getMessage());
    }
}

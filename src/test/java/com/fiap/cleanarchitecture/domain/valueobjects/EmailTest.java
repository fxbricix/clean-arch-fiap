package com.fiap.cleanarchitecture.domain.valueobjects;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    @DisplayName("Deve normalizar o e-mail removendo espaços e convertendo para minúsculas")
    void deveNormalizarEmail() {
        Email email = new Email("  Usuario@Fiap.com  ");

        assertEquals("usuario@fiap.com", email.valor());
    }

    @Test
    @DisplayName("Deve aceitar e-mail com o tamanho máximo permitido")
    void deveAceitarEmailComTamanhoMaximo() {
        String valor = "a".repeat(155) + "@b.co";

        Email email = new Email(valor);

        assertEquals(Email.TAMANHO_MAXIMO, email.valor().length());
        assertEquals(valor, email.valor());
    }

    @Test
    @DisplayName("Não deve aceitar e-mail nulo")
    void naoDeveAceitarEmailNulo() {
        DominioException exception = assertThrows(DominioException.class, () -> new Email(null));

        assertEquals("O e-mail é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar e-mail em branco")
    void naoDeveAceitarEmailEmBranco() {
        DominioException exception = assertThrows(DominioException.class, () -> new Email("   "));

        assertEquals("O e-mail é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar e-mail maior que o tamanho máximo")
    void naoDeveAceitarEmailMuitoLongo() {
        String valor = "a".repeat(156) + "@b.co";

        DominioException exception = assertThrows(DominioException.class, () -> new Email(valor));

        assertEquals("O e-mail deve ter no máximo 160 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar e-mail em formato inválido")
    void naoDeveAceitarEmailInvalido() {
        DominioException exception = assertThrows(DominioException.class, () -> new Email("usuario@fiap"));

        assertEquals("O e-mail informado é inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar e-mail contendo espaços")
    void naoDeveAceitarEmailComEspacos() {
        DominioException exception = assertThrows(DominioException.class, () -> new Email("usuario @fiap.com"));

        assertEquals("O e-mail informado é inválido", exception.getMessage());
    }
}

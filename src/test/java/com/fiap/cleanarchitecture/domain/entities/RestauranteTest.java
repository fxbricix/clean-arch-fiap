package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestauranteTest {

    private static final HorarioFuncionamento HORARIO =
            new HorarioFuncionamento(LocalTime.of(9, 0), LocalTime.of(18, 0));

    private Restaurante criar(String nome, String endereco, String tipoCozinha,
                              HorarioFuncionamento horario, Long idDono) {
        return new Restaurante(1L, nome, endereco, tipoCozinha, horario, idDono);
    }

    private Restaurante restauranteValido() {
        return criar("Restaurante", "Endereço", "Brasileira", HORARIO, 10L);
    }

    private void assertEstadoOriginal(Restaurante r) {
        assertEquals("Restaurante", r.getNome());
        assertEquals("Endereço", r.getEndereco());
        assertEquals("Brasileira", r.getTipoCozinha());
        assertEquals(HORARIO, r.getHorarioFuncionamento());
        assertEquals(10L, r.getIdDono());
    }

    @Test
    @DisplayName("Deve criar restaurante com dados válidos")
    void deveCriarRestaurante() {
        Restaurante r = restauranteValido();

        assertEquals(1L, r.getId());
        assertEstadoOriginal(r);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Não deve criar restaurante com nome nulo ou em branco")
    void naoDeveCriarComNomeNuloOuEmBranco(String nome) {
        assertThrows(DominioException.class, () -> criar(nome, "Endereço", "Brasileira", HORARIO, 10L));
    }

    @Test
    @DisplayName("Não deve criar restaurante com nome acima do limite")
    void naoDeveCriarComNomeAcimaDoLimite() {
        String nome = "a".repeat(Restaurante.TAMANHO_MAXIMO_NOME + 1);

        assertThrows(DominioException.class, () -> criar(nome, "Endereço", "Brasileira", HORARIO, 10L));
    }

    @Test
    @DisplayName("Deve aceitar nome exatamente no limite")
    void deveAceitarNomeNoLimite() {
        String nome = "a".repeat(Restaurante.TAMANHO_MAXIMO_NOME);

        assertEquals(nome, criar(nome, "Endereço", "Brasileira", HORARIO, 10L).getNome());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Não deve criar restaurante com endereço nulo ou em branco")
    void naoDeveCriarComEnderecoNuloOuEmBranco(String endereco) {
        assertThrows(DominioException.class, () -> criar("Restaurante", endereco, "Brasileira", HORARIO, 10L));
    }

    @Test
    @DisplayName("Não deve criar restaurante com endereço acima do limite")
    void naoDeveCriarComEnderecoAcimaDoLimite() {
        String endereco = "a".repeat(Restaurante.TAMANHO_MAXIMO_ENDERECO + 1);

        assertThrows(DominioException.class, () -> criar("Restaurante", endereco, "Brasileira", HORARIO, 10L));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Não deve criar restaurante com tipo de cozinha nulo ou em branco")
    void naoDeveCriarComTipoCozinhaNuloOuEmBranco(String tipo) {
        assertThrows(DominioException.class, () -> criar("Restaurante", "Endereço", tipo, HORARIO, 10L));
    }

    @Test
    @DisplayName("Não deve criar restaurante com tipo de cozinha acima do limite")
    void naoDeveCriarComTipoCozinhaAcimaDoLimite() {
        String tipo = "a".repeat(Restaurante.TAMANHO_MAXIMO_TIPO_COZINHA + 1);

        assertThrows(DominioException.class, () -> criar("Restaurante", "Endereço", tipo, HORARIO, 10L));
    }

    @Test
    @DisplayName("Deve remover espaços excedentes de nome, endereço e tipo de cozinha")
    void deveAplicarTrim() {
        Restaurante r = criar("  Restaurante  ", "  Endereço  ", "  Brasileira  ", HORARIO, 10L);

        assertEstadoOriginal(r);
    }

    @Test
    @DisplayName("Não deve criar restaurante sem horário de funcionamento")
    void naoDeveCriarSemHorario() {
        assertThrows(DominioException.class, () -> criar("Restaurante", "Endereço", "Brasileira", null, 10L));
    }

    @Test
    @DisplayName("Não deve criar restaurante sem dono")
    void naoDeveCriarSemDono() {
        assertThrows(DominioException.class, () -> criar("Restaurante", "Endereço", "Brasileira", HORARIO, null));
    }

    @Test
    @DisplayName("alterarDados válido deve atualizar todos os campos, incluindo o dono")
    void alterarDadosValidoAtualizaTudo() {
        Restaurante r = restauranteValido();
        HorarioFuncionamento novoHorario = new HorarioFuncionamento(LocalTime.of(11, 0), LocalTime.of(23, 0));

        r.alterarDados("  Novo nome  ", "Novo endereço", "Italiana", novoHorario, 20L);

        assertEquals(1L, r.getId());
        assertEquals("Novo nome", r.getNome());
        assertEquals("Novo endereço", r.getEndereco());
        assertEquals("Italiana", r.getTipoCozinha());
        assertEquals(novoHorario, r.getHorarioFuncionamento());
        assertEquals(20L, r.getIdDono());
    }

    @Test
    @DisplayName("alterarDados com nome inválido não deve alterar nenhum campo")
    void alterarDadosComNomeInvalidoNaoAltera() {
        Restaurante r = restauranteValido();
        HorarioFuncionamento novoHorario = new HorarioFuncionamento(LocalTime.of(11, 0), LocalTime.of(23, 0));

        assertThrows(DominioException.class,
                () -> r.alterarDados(" ", "Novo endereço", "Italiana", novoHorario, 20L));

        assertEstadoOriginal(r);
    }

    @Test
    @DisplayName("alterarDados com último campo inválido (dono nulo) não deve alterar nenhum campo")
    void alterarDadosComDonoNuloNaoAltera() {
        Restaurante r = restauranteValido();
        HorarioFuncionamento novoHorario = new HorarioFuncionamento(LocalTime.of(11, 0), LocalTime.of(23, 0));

        assertThrows(DominioException.class,
                () -> r.alterarDados("Novo nome", "Novo endereço", "Italiana", novoHorario, null));

        assertEstadoOriginal(r);
    }

    @Test
    @DisplayName("alterarDados com horário nulo não deve alterar nenhum campo")
    void alterarDadosComHorarioNuloNaoAltera() {
        Restaurante r = restauranteValido();

        assertThrows(DominioException.class,
                () -> r.alterarDados("Novo nome", "Novo endereço", "Italiana", null, 20L));

        assertEstadoOriginal(r);
    }
}

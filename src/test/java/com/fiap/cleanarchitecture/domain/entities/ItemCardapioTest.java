package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemCardapioTest {

    private static final BigDecimal PRECO = new BigDecimal("39.90");

    private ItemCardapio criar(String nome, String descricao, BigDecimal preco, String foto, Long idRestaurante) {
        return new ItemCardapio(1L, nome, descricao, preco, false, foto, idRestaurante);
    }

    private ItemCardapio itemValido() {
        return criar("Lasanha", "Molho branco", PRECO, "/fotos/a.jpg", 7L);
    }

    private void assertEstadoOriginal(ItemCardapio item) {
        assertEquals(1L, item.getId());
        assertEquals("Lasanha", item.getNome());
        assertEquals("Molho branco", item.getDescricao());
        assertEquals(PRECO, item.getPreco());
        assertFalse(item.isApenasNoLocal());
        assertEquals("/fotos/a.jpg", item.getCaminhoFoto());
        assertEquals(7L, item.getIdRestaurante());
    }

    @Test
    @DisplayName("Deve criar item com dados válidos")
    void deveCriarItem() {
        ItemCardapio item = new ItemCardapio(1L, "Lasanha", "Molho branco", PRECO, true, "/fotos/a.jpg", 7L);

        assertEquals(1L, item.getId());
        assertEquals("Lasanha", item.getNome());
        assertEquals("Molho branco", item.getDescricao());
        assertEquals(PRECO, item.getPreco());
        assertTrue(item.isApenasNoLocal());
        assertEquals("/fotos/a.jpg", item.getCaminhoFoto());
        assertEquals(7L, item.getIdRestaurante());
    }

    @Test
    @DisplayName("Deve remover espaços nas pontas do nome e da descrição")
    void deveAplicarTrimNomeEDescricao() {
        ItemCardapio item = criar("  Lasanha  ", "  Molho branco  ", PRECO, null, 7L);

        assertEquals("Lasanha", item.getNome());
        assertEquals("Molho branco", item.getDescricao());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Não deve criar item com nome nulo ou em branco")
    void naoDeveCriarComNomeInvalido(String nome) {
        DominioException e = assertThrows(DominioException.class,
                () -> criar(nome, "Molho branco", PRECO, null, 7L));
        assertEquals("O nome do item do cardápio é obrigatório", e.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar nome no limite e rejeitar acima do limite")
    void deveValidarLimiteDoNome() {
        String noLimite = "a".repeat(ItemCardapio.TAMANHO_MAXIMO_NOME);

        assertEquals(noLimite, criar(noLimite, "Molho branco", PRECO, null, 7L).getNome());
        assertThrows(DominioException.class, () -> criar(noLimite + "a", "Molho branco", PRECO, null, 7L));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Não deve criar item com descrição nula ou em branco")
    void naoDeveCriarComDescricaoInvalida(String descricao) {
        DominioException e = assertThrows(DominioException.class,
                () -> criar("Lasanha", descricao, PRECO, null, 7L));
        assertEquals("A descrição do item do cardápio é obrigatória", e.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar descrição no limite e rejeitar acima do limite")
    void deveValidarLimiteDaDescricao() {
        String noLimite = "a".repeat(ItemCardapio.TAMANHO_MAXIMO_DESCRICAO);

        assertEquals(noLimite, criar("Lasanha", noLimite, PRECO, null, 7L).getDescricao());
        assertThrows(DominioException.class, () -> criar("Lasanha", noLimite + "a", PRECO, null, 7L));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"0", "0.00", "-1", "-0.01"})
    @DisplayName("Não deve criar item com preço nulo, zero ou negativo")
    void naoDeveCriarComPrecoInvalido(String preco) {
        BigDecimal valor = preco == null ? null : new BigDecimal(preco);

        DominioException e = assertThrows(DominioException.class,
                () -> criar("Lasanha", "Molho branco", valor, null, 7L));
        assertEquals("O preço do item do cardápio deve ser maior que zero", e.getMessage());
    }

    @Test
    @DisplayName("Não deve criar item com mais de duas casas decimais no preço")
    void naoDeveCriarComTresCasasDecimais() {
        DominioException e = assertThrows(DominioException.class,
                () -> criar("Lasanha", "Molho branco", new BigDecimal("10.999"), null, 7L));
        assertEquals("O preço do item do cardápio deve ter no máximo 2 casas decimais", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10.5", "10.50", "10.500", "10", "0.01"})
    @DisplayName("Deve aceitar preço com até duas casas decimais significativas")
    void deveAceitarPrecoValido(String preco) {
        BigDecimal valor = new BigDecimal(preco);

        assertEquals(valor, criar("Lasanha", "Molho branco", valor, null, 7L).getPreco());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("Deve transformar foto nula ou em branco em nulo")
    void deveTransformarFotoVaziaEmNulo(String foto) {
        assertNull(criar("Lasanha", "Molho branco", PRECO, foto, 7L).getCaminhoFoto());
    }

    @Test
    @DisplayName("Deve remover espaços nas pontas do caminho da foto")
    void deveAplicarTrimNaFoto() {
        assertEquals("/fotos/a.jpg", criar("Lasanha", "Molho branco", PRECO, "  /fotos/a.jpg  ", 7L).getCaminhoFoto());
    }

    @Test
    @DisplayName("Deve aceitar foto no limite e rejeitar acima do limite")
    void deveValidarLimiteDaFoto() {
        String noLimite = "a".repeat(ItemCardapio.TAMANHO_MAXIMO_CAMINHO_FOTO);

        assertEquals(noLimite, criar("Lasanha", "Molho branco", PRECO, noLimite, 7L).getCaminhoFoto());
        assertThrows(DominioException.class, () -> criar("Lasanha", "Molho branco", PRECO, noLimite + "a", 7L));
    }

    @Test
    @DisplayName("Não deve criar item sem restaurante")
    void naoDeveCriarSemRestaurante() {
        DominioException e = assertThrows(DominioException.class,
                () -> criar("Lasanha", "Molho branco", PRECO, null, null));
        assertEquals("O restaurante do item do cardápio é obrigatório", e.getMessage());
    }

    @Test
    @DisplayName("Deve alterar todos os dados mantendo id e restaurante")
    void deveAlterarDados() {
        ItemCardapio item = itemValido();

        item.alterarDados("  Pizza  ", "  Mussarela  ", new BigDecimal("45.50"), true, "  /fotos/b.jpg  ");

        assertEquals("Pizza", item.getNome());
        assertEquals("Mussarela", item.getDescricao());
        assertEquals(new BigDecimal("45.50"), item.getPreco());
        assertTrue(item.isApenasNoLocal());
        assertEquals("/fotos/b.jpg", item.getCaminhoFoto());
        assertEquals(1L, item.getId());
        assertEquals(7L, item.getIdRestaurante());
    }

    @Test
    @DisplayName("Não deve alterar nada quando o último campo é inválido")
    void naoDeveAlterarQuandoFotoInvalida() {
        ItemCardapio item = itemValido();
        String fotoLonga = "a".repeat(ItemCardapio.TAMANHO_MAXIMO_CAMINHO_FOTO + 1);

        assertThrows(DominioException.class,
                () -> item.alterarDados("Pizza", "Mussarela", new BigDecimal("45.50"), true, fotoLonga));

        assertEstadoOriginal(item);
    }
}

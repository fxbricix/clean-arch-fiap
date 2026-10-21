package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.application.interfaces.IItemCardapioUseCase;
import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.infrastructure.web.controllers.ItemCardapioController;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarItemCardapioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.ItemCardapioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemCardapioControllerTest {

    private static final long RESTAURANTE_ID = 7L;
    private static final long USUARIO_ID = 10L;

    private IItemCardapioUseCase useCase;
    private ItemCardapioController controller;

    @BeforeEach
    void setUp() {
        useCase = mock(IItemCardapioUseCase.class);
        controller = new ItemCardapioController(useCase);
    }

    @Test
    @DisplayName("Deve criar item e responder com status criado")
    void deveCriarItem() {
        when(useCase.criar(RESTAURANTE_ID, USUARIO_ID, "Lasanha", "Molho branco", new BigDecimal("39.90"),
                true, "/fotos/a.jpg")).thenReturn(item(1L, "Lasanha"));

        ResponseEntity<ItemCardapioDTO> resposta = controller.criarItem(RESTAURANTE_ID, USUARIO_ID,
                requisicao("Lasanha"));

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(itemDTO(1L, "Lasanha"), resposta.getBody());
        verify(useCase).criar(RESTAURANTE_ID, USUARIO_ID, "Lasanha", "Molho branco", new BigDecimal("39.90"),
                true, "/fotos/a.jpg");
    }

    @Test
    @DisplayName("Deve buscar item pelo ID dentro do restaurante")
    void deveBuscarItemPorId() {
        when(useCase.buscarPorId(RESTAURANTE_ID, 1L)).thenReturn(item(1L, "Lasanha"));

        assertEquals(itemDTO(1L, "Lasanha"), controller.buscarPorId(RESTAURANTE_ID, 1L));
        verify(useCase).buscarPorId(RESTAURANTE_ID, 1L);
    }

    @Test
    @DisplayName("Deve listar itens do restaurante")
    void deveListarItens() {
        when(useCase.listar(RESTAURANTE_ID)).thenReturn(List.of(item(1L, "Lasanha"), item(2L, "Pizza")));

        List<ItemCardapioDTO> resposta = controller.listarItens(RESTAURANTE_ID);

        assertEquals(List.of(itemDTO(1L, "Lasanha"), itemDTO(2L, "Pizza")), resposta);
        verify(useCase).listar(RESTAURANTE_ID);
    }

    @Test
    @DisplayName("Deve atualizar item")
    void deveAtualizarItem() {
        when(useCase.atualizar(RESTAURANTE_ID, 1L, USUARIO_ID, "Pizza", "Molho branco", new BigDecimal("39.90"),
                true, "/fotos/a.jpg")).thenReturn(item(1L, "Pizza"));

        ItemCardapioDTO resposta = controller.atualizarItem(RESTAURANTE_ID, 1L, USUARIO_ID, requisicao("Pizza"));

        assertEquals(itemDTO(1L, "Pizza"), resposta);
    }

    @Test
    @DisplayName("Deve excluir item")
    void deveExcluirItem() {
        controller.excluirItem(RESTAURANTE_ID, 1L, USUARIO_ID);

        verify(useCase).excluir(RESTAURANTE_ID, 1L, USUARIO_ID);
    }

    private static CriarItemCardapioDTO requisicao(String nome) {
        return new CriarItemCardapioDTO(nome, "Molho branco", new BigDecimal("39.90"), true, "/fotos/a.jpg");
    }

    private static ItemCardapio item(Long id, String nome) {
        return new ItemCardapio(id, nome, "Molho branco", new BigDecimal("39.90"), true, "/fotos/a.jpg",
                RESTAURANTE_ID);
    }

    private static ItemCardapioDTO itemDTO(Long id, String nome) {
        return new ItemCardapioDTO(id, nome, "Molho branco", new BigDecimal("39.90"), true, "/fotos/a.jpg",
                RESTAURANTE_ID);
    }
}

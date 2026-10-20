package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.application.interfaces.IRestauranteUseCase;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.infrastructure.web.controllers.RestauranteController;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarRestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.HorarioFuncionamentoDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.RestauranteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestauranteControllerTest {

    private IRestauranteUseCase useCase;
    private RestauranteController controller;

    @BeforeEach
    void setUp() {
        useCase = mock(IRestauranteUseCase.class);
        controller = new RestauranteController(useCase);
    }

    @Test
    @DisplayName("Deve criar restaurante e responder com status criado")
    void deveCriarRestaurante() {
        when(useCase.criar(any(Restaurante.class))).thenReturn(restaurante(1L, "Cantina"));

        ResponseEntity<RestauranteDTO> resposta = controller.criarRestaurante(requisicao("Cantina"));

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(restauranteDTO(1L, "Cantina"), resposta.getBody());
        verify(useCase).criar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve buscar restaurante pelo ID")
    void deveBuscarRestaurantePorId() {
        when(useCase.buscarPorId(1L)).thenReturn(restaurante(1L, "Cantina"));

        RestauranteDTO resposta = controller.buscarPorId(1L);

        assertEquals(restauranteDTO(1L, "Cantina"), resposta);
        verify(useCase).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve listar restaurantes")
    void deveListarRestaurantes() {
        when(useCase.listar()).thenReturn(List.of(restaurante(1L, "Cantina"), restaurante(2L, "Bistrô")));

        List<RestauranteDTO> resposta = controller.listarRestaurantes();

        assertEquals(List.of(restauranteDTO(1L, "Cantina"), restauranteDTO(2L, "Bistrô")), resposta);
        verify(useCase).listar();
    }

    @Test
    @DisplayName("Deve atualizar restaurante")
    void deveAtualizarRestaurante() {
        when(useCase.atualizar(eq(1L), any(Restaurante.class))).thenReturn(restaurante(1L, "Novo Nome"));

        RestauranteDTO resposta = controller.atualizarRestaurante(1L, requisicao("Novo Nome"));

        assertEquals(restauranteDTO(1L, "Novo Nome"), resposta);
        verify(useCase).atualizar(eq(1L), any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve excluir restaurante")
    void deveExcluirRestaurante() {
        controller.excluirRestaurante(1L);

        verify(useCase).excluir(1L);
    }

    private static CriarRestauranteDTO requisicao(String nome) {
        return new CriarRestauranteDTO(nome, "Rua A, 10", "Italiana",
                new HorarioFuncionamentoDTO(LocalTime.of(10, 0), LocalTime.of(22, 0)), 5L);
    }

    private static Restaurante restaurante(Long id, String nome) {
        return new Restaurante(id, nome, "Rua A, 10", "Italiana",
                new HorarioFuncionamento(LocalTime.of(10, 0), LocalTime.of(22, 0)), 5L);
    }

    private static RestauranteDTO restauranteDTO(Long id, String nome) {
        return new RestauranteDTO(id, nome, "Rua A, 10", "Italiana",
                new HorarioFuncionamentoDTO(LocalTime.of(10, 0), LocalTime.of(22, 0)), 5L);
    }
}

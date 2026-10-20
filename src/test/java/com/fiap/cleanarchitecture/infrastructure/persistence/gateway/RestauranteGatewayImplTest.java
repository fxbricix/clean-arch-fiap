package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.RestauranteEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.RestauranteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestauranteGatewayImplTest {

    private RestauranteJpaRepository repository;
    private RestauranteGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        repository = mock(RestauranteJpaRepository.class);
        gateway = new RestauranteGatewayImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar restaurante com sucesso")
    void deveSalvarRestaurante() {
        when(repository.save(any(RestauranteEntity.class))).thenReturn(restauranteEntity(1L, "Cantina", 5L));

        Restaurante resultado = gateway.salvar(restaurante(null, "Cantina", 5L));

        assertEquals(1L, resultado.getId());
        assertEquals("Cantina", resultado.getNome());
        assertEquals(5L, resultado.getIdDono());
        assertEquals(LocalTime.of(10, 0), resultado.getHorarioFuncionamento().abertura());
        verify(repository).save(any(RestauranteEntity.class));
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID")
    void deveBuscarRestaurantePorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(restauranteEntity(1L, "Cantina", 5L)));

        Optional<Restaurante> resultado = gateway.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Cantina", resultado.get().getNome());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar restaurante inexistente")
    void deveRetornarVazioAoBuscarRestauranteInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(gateway.buscarPorId(99L).isEmpty());
    }

    @Test
    @DisplayName("Deve listar restaurantes")
    void deveListarRestaurantes() {
        when(repository.findAll())
                .thenReturn(List.of(restauranteEntity(1L, "Cantina", 5L), restauranteEntity(2L, "Bistrô", 6L)));

        List<Restaurante> resultado = gateway.listar();

        assertEquals(2, resultado.size());
        assertEquals("Cantina", resultado.get(0).getNome());
        assertEquals("Bistrô", resultado.get(1).getNome());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve excluir restaurante e sincronizar com o banco")
    void deveExcluirRestaurante() {
        gateway.excluir(1L);

        verify(repository).deleteById(1L);
        verify(repository).flush();
    }

    @Test
    @DisplayName("Deve converter violação de integridade ao excluir em conflito")
    void deveLancarConflitoAoExcluir() {
        doThrow(new DataIntegrityViolationException("violação de integridade"))
                .when(repository).deleteById(1L);

        ConflitoException exception = assertThrows(ConflitoException.class, () -> gateway.excluir(1L));

        assertEquals("Não foi possível excluir restaurante", exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se existe restaurante pelo dono")
    void deveVerificarExistenciaPorDonoId() {
        when(repository.existsByDonoId(5L)).thenReturn(true);
        when(repository.existsByDonoId(6L)).thenReturn(false);

        assertTrue(gateway.existePorDonoId(5L));
        assertFalse(gateway.existePorDonoId(6L));
        verify(repository).existsByDonoId(5L);
        verify(repository).existsByDonoId(6L);
    }

    private static Restaurante restaurante(Long id, String nome, Long donoId) {
        return new Restaurante(id, nome, "Rua A, 10", "Italiana",
                new HorarioFuncionamento(LocalTime.of(10, 0), LocalTime.of(22, 0)), donoId);
    }

    private static RestauranteEntity restauranteEntity(Long id, String nome, Long donoId) {
        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(id);
        entity.setNome(nome);
        entity.setEndereco("Rua A, 10");
        entity.setTipoCozinha("Italiana");
        entity.setHoraAbertura(LocalTime.of(10, 0));
        entity.setHoraFechamento(LocalTime.of(22, 0));
        entity.setDonoId(donoId);
        return entity;
    }
}

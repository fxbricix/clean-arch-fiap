package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.ItemCardapioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.ItemCardapioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemCardapioGatewayImplTest {

    private ItemCardapioJpaRepository repository;
    private ItemCardapioGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        repository = mock(ItemCardapioJpaRepository.class);
        gateway = new ItemCardapioGatewayImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar item do cardápio com sucesso")
    void deveSalvarItem() {
        when(repository.save(any(ItemCardapioEntity.class))).thenReturn(entity(1L, "Lasanha", 7L));

        ItemCardapio resultado = gateway.salvar(item(null, "Lasanha", 7L));

        assertEquals(1L, resultado.getId());
        assertEquals("Lasanha", resultado.getNome());
        assertEquals(7L, resultado.getIdRestaurante());
        verify(repository).save(any(ItemCardapioEntity.class));
    }

    @Test
    @DisplayName("Deve buscar item pelo ID e pelo restaurante")
    void deveBuscarItemPorIdERestauranteId() {
        when(repository.findByIdAndRestauranteId(1L, 7L)).thenReturn(Optional.of(entity(1L, "Lasanha", 7L)));

        Optional<ItemCardapio> resultado = gateway.buscarPorIdERestauranteId(1L, 7L);

        assertTrue(resultado.isPresent());
        assertEquals("Lasanha", resultado.get().getNome());
        verify(repository).findByIdAndRestauranteId(1L, 7L);
    }

    @Test
    @DisplayName("Deve retornar vazio quando o item não existe no restaurante")
    void deveRetornarVazioAoBuscarItemInexistente() {
        when(repository.findByIdAndRestauranteId(1L, 8L)).thenReturn(Optional.empty());

        assertTrue(gateway.buscarPorIdERestauranteId(1L, 8L).isEmpty());
    }

    @Test
    @DisplayName("Deve listar itens do restaurante")
    void deveListarItensDoRestaurante() {
        when(repository.findByRestauranteIdOrderByIdAsc(7L))
                .thenReturn(List.of(entity(1L, "Lasanha", 7L), entity(2L, "Suco", 7L)));

        List<ItemCardapio> resultado = gateway.listarPorRestauranteId(7L);

        assertEquals(2, resultado.size());
        assertEquals("Lasanha", resultado.get(0).getNome());
        assertEquals("Suco", resultado.get(1).getNome());
        verify(repository).findByRestauranteIdOrderByIdAsc(7L);
    }

    @Test
    @DisplayName("Deve excluir item e sincronizar com o banco")
    void deveExcluirItem() {
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

        assertEquals("Não foi possível excluir item do cardápio", exception.getMessage());
    }

    private static ItemCardapio item(Long id, String nome, Long restauranteId) {
        return new ItemCardapio(id, nome, "Descrição", new BigDecimal("10.00"), false, null, restauranteId);
    }

    private static ItemCardapioEntity entity(Long id, String nome, Long restauranteId) {
        ItemCardapioEntity entity = new ItemCardapioEntity();
        entity.setId(id);
        entity.setNome(nome);
        entity.setDescricao("Descrição");
        entity.setPreco(new BigDecimal("10.00"));
        entity.setRestauranteId(restauranteId);
        return entity;
    }
}

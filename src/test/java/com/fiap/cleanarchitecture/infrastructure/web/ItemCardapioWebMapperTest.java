package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.ItemCardapioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.mappers.ItemCardapioWebMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemCardapioWebMapperTest {

    private final ItemCardapioWebMapper mapper = new ItemCardapioWebMapper();

    @Test
    @DisplayName("Deve converter entidade de domínio para DTO")
    void deveConverterParaDTO() {
        ItemCardapio item = new ItemCardapio(1L, "Lasanha", "Molho branco", new BigDecimal("39.90"),
                true, "/fotos/a.jpg", 7L);

        ItemCardapioDTO dto = mapper.toDTO(item);

        assertEquals(new ItemCardapioDTO(1L, "Lasanha", "Molho branco", new BigDecimal("39.90"),
                true, "/fotos/a.jpg", 7L), dto);
    }

    @Test
    @DisplayName("Deve converter item sem foto mantendo o caminho nulo")
    void deveConverterItemSemFoto() {
        ItemCardapio item = new ItemCardapio(1L, "Suco", "Laranja", new BigDecimal("8.50"), false, null, 7L);

        assertNull(mapper.toDTO(item).caminhoFoto());
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter entidade de domínio nula")
    void deveRetornarNuloAoConverterEntidadeNula() {
        assertNull(mapper.toDTO(null));
    }
}

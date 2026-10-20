package com.fiap.cleanarchitecture.infrastructure.persistence.mappers;

import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.ItemCardapioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemCardapioPersistenceMapperTest {

    private final ItemCardapioPersistenceMapper mapper = new ItemCardapioPersistenceMapper();

    @Test
    @DisplayName("Deve converter entidade de persistência para entidade de domínio")
    void deveConverterParaEntidadeDeDominio() {
        ItemCardapio item = mapper.toDomainEntity(entity(1L, "Lasanha", "Molho branco", "39.90", true, "/fotos/a.jpg", 7L));

        assertEquals(1L, item.getId());
        assertEquals("Lasanha", item.getNome());
        assertEquals("Molho branco", item.getDescricao());
        assertEquals(new BigDecimal("39.90"), item.getPreco());
        assertTrue(item.isApenasNoLocal());
        assertEquals("/fotos/a.jpg", item.getCaminhoFoto());
        assertEquals(7L, item.getIdRestaurante());
    }

    @Test
    @DisplayName("Deve converter entidade de domínio para entidade de persistência")
    void deveConverterParaEntidadeDePersistencia() {
        ItemCardapio item = new ItemCardapio(1L, "Lasanha", "Molho branco", new BigDecimal("39.90"),
                true, "/fotos/a.jpg", 7L);

        ItemCardapioEntity entity = mapper.toEntity(item);

        assertEquals(1L, entity.getId());
        assertEquals("Lasanha", entity.getNome());
        assertEquals("Molho branco", entity.getDescricao());
        assertEquals(new BigDecimal("39.90"), entity.getPreco());
        assertTrue(entity.isApenasNoLocal());
        assertEquals("/fotos/a.jpg", entity.getCaminhoFoto());
        assertEquals(7L, entity.getRestauranteId());
    }

    @Test
    @DisplayName("Deve preservar o preço com duas casas na ida e volta")
    void devePreservarPrecoNaIdaEVolta() {
        ItemCardapio original = new ItemCardapio(2L, "Suco", "Laranja", new BigDecimal("8.50"), false, null, 7L);

        ItemCardapio convertido = mapper.toDomainEntity(mapper.toEntity(original));

        assertEquals(new BigDecimal("8.50"), convertido.getPreco());
        assertEquals(original.getIdRestaurante(), convertido.getIdRestaurante());
    }

    @Test
    @DisplayName("Deve converter caminho da foto nulo nos dois sentidos")
    void deveConverterCaminhoFotoNulo() {
        ItemCardapio item = mapper.toDomainEntity(entity(1L, "Lasanha", "Molho branco", "39.90", false, null, 7L));
        assertNull(item.getCaminhoFoto());

        assertNull(mapper.toEntity(item).getCaminhoFoto());
    }

    @Test
    @DisplayName("Descrição nula no banco é rejeitada pelo domínio, que exige descrição")
    void deveRejeitarDescricaoNulaVindaDoBanco() {
        ItemCardapioEntity entity = entity(1L, "Lasanha", null, "39.90", false, null, 7L);

        assertThrows(DominioException.class, () -> mapper.toDomainEntity(entity));
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter entidade de persistência nula")
    void deveRetornarNuloAoConverterEntidadeDePersistenciaNula() {
        assertNull(mapper.toDomainEntity(null));
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter entidade de domínio nula")
    void deveRetornarNuloAoConverterEntidadeDeDominioNula() {
        assertNull(mapper.toEntity(null));
    }

    private static ItemCardapioEntity entity(Long id, String nome, String descricao, String preco,
                                             boolean apenasNoLocal, String foto, Long restauranteId) {
        ItemCardapioEntity entity = new ItemCardapioEntity();
        entity.setId(id);
        entity.setNome(nome);
        entity.setDescricao(descricao);
        entity.setPreco(new BigDecimal(preco));
        entity.setApenasNoLocal(apenasNoLocal);
        entity.setCaminhoFoto(foto);
        entity.setRestauranteId(restauranteId);
        return entity;
    }
}

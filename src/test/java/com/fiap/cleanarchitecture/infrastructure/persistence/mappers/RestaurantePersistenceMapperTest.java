package com.fiap.cleanarchitecture.infrastructure.persistence.mappers;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.RestauranteEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RestaurantePersistenceMapperTest {

    private final RestaurantePersistenceMapper mapper = new RestaurantePersistenceMapper();

    @Test
    @DisplayName("Deve converter entidade de persistência para entidade de domínio")
    void deveConverterParaEntidadeDeDominio() {
        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(1L);
        entity.setNome("Cantina");
        entity.setEndereco("Rua A, 10");
        entity.setTipoCozinha("Italiana");
        entity.setHoraAbertura(LocalTime.of(10, 0));
        entity.setHoraFechamento(LocalTime.of(22, 30));
        entity.setDonoId(5L);

        Restaurante restaurante = mapper.toDomainEntity(entity);

        assertEquals(1L, restaurante.getId());
        assertEquals("Cantina", restaurante.getNome());
        assertEquals("Rua A, 10", restaurante.getEndereco());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals(LocalTime.of(10, 0), restaurante.getHorarioFuncionamento().abertura());
        assertEquals(LocalTime.of(22, 30), restaurante.getHorarioFuncionamento().fechamento());
        assertEquals(5L, restaurante.getIdDono());
    }

    @Test
    @DisplayName("Deve converter entidade de domínio para entidade de persistência")
    void deveConverterParaEntidadeDePersistencia() {
        Restaurante restaurante = new Restaurante(1L, "Cantina", "Rua A, 10", "Italiana",
                new HorarioFuncionamento(LocalTime.of(10, 0), LocalTime.of(22, 30)), 5L);

        RestauranteEntity entity = mapper.toEntity(restaurante);

        assertEquals(1L, entity.getId());
        assertEquals("Cantina", entity.getNome());
        assertEquals("Rua A, 10", entity.getEndereco());
        assertEquals("Italiana", entity.getTipoCozinha());
        assertEquals(LocalTime.of(10, 0), entity.getHoraAbertura());
        assertEquals(LocalTime.of(22, 30), entity.getHoraFechamento());
        assertEquals(5L, entity.getDonoId());
    }

    @Test
    @DisplayName("Deve preservar horário e dono na conversão de ida e volta")
    void devePreservarHorarioEDonoNaIdaEVolta() {
        Restaurante original = new Restaurante(2L, "Bistrô", "Rua B, 20", "Francesa",
                new HorarioFuncionamento(LocalTime.of(8, 15), LocalTime.of(23, 45)), 7L);

        Restaurante convertido = mapper.toDomainEntity(mapper.toEntity(original));

        assertEquals(original.getHorarioFuncionamento(), convertido.getHorarioFuncionamento());
        assertEquals(original.getIdDono(), convertido.getIdDono());
        assertEquals(original.getId(), convertido.getId());
        assertEquals(original.getNome(), convertido.getNome());
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
}

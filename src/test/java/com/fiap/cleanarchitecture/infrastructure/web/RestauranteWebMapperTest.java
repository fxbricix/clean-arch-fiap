package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarRestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.HorarioFuncionamentoDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.RestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.mappers.RestauranteWebMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestauranteWebMapperTest {

    private final RestauranteWebMapper mapper = new RestauranteWebMapper();

    @Test
    @DisplayName("Deve converter DTO de criação para entidade de domínio sem ID")
    void deveConverterParaEntidadeDeDominio() {
        CriarRestauranteDTO dto = new CriarRestauranteDTO("Cantina", "Rua A, 10", "Italiana",
                new HorarioFuncionamentoDTO(LocalTime.of(10, 0), LocalTime.of(22, 30)), 5L);

        Restaurante restaurante = mapper.toDomainEntity(dto);

        assertNull(restaurante.getId());
        assertEquals("Cantina", restaurante.getNome());
        assertEquals("Rua A, 10", restaurante.getEndereco());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals(new HorarioFuncionamento(LocalTime.of(10, 0), LocalTime.of(22, 30)),
                restaurante.getHorarioFuncionamento());
        assertEquals(5L, restaurante.getIdDono());
    }

    @Test
    @DisplayName("Deve converter entidade de domínio para DTO")
    void deveConverterParaDTO() {
        Restaurante restaurante = new Restaurante(1L, "Cantina", "Rua A, 10", "Italiana",
                new HorarioFuncionamento(LocalTime.of(10, 0), LocalTime.of(22, 30)), 5L);

        RestauranteDTO dto = mapper.toDTO(restaurante);

        assertEquals(new RestauranteDTO(1L, "Cantina", "Rua A, 10", "Italiana",
                new HorarioFuncionamentoDTO(LocalTime.of(10, 0), LocalTime.of(22, 30)), 5L), dto);
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter DTO nulo")
    void deveRetornarNuloAoConverterDtoNulo() {
        assertNull(mapper.toDomainEntity(null));
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter entidade de domínio nula")
    void deveRetornarNuloAoConverterEntidadeNula() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    @DisplayName("Deve rejeitar DTO sem horário de funcionamento")
    void deveRejeitarDtoSemHorario() {
        CriarRestauranteDTO dto = new CriarRestauranteDTO("Cantina", "Rua A, 10", "Italiana", null, 5L);

        assertThrows(DominioException.class, () -> mapper.toDomainEntity(dto));
    }
}

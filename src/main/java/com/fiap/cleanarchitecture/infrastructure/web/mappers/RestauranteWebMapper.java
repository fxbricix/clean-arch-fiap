package com.fiap.cleanarchitecture.infrastructure.web.mappers;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarRestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.HorarioFuncionamentoDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.RestauranteDTO;

public class RestauranteWebMapper {

    public Restaurante toDomainEntity(CriarRestauranteDTO dto) {
        if (dto == null) {
            return null;
        }
        HorarioFuncionamentoDTO horario = dto.horarioFuncionamento();
        return new Restaurante(
                null,
                dto.nome(),
                dto.endereco(),
                dto.tipoCozinha(),
                horario == null ? null : new HorarioFuncionamento(horario.abertura(), horario.fechamento()),
                dto.donoId());
    }

    public RestauranteDTO toDTO(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }
        HorarioFuncionamento horario = restaurante.getHorarioFuncionamento();
        return new RestauranteDTO(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getTipoCozinha(),
                new HorarioFuncionamentoDTO(horario.abertura(), horario.fechamento()),
                restaurante.getIdDono());
    }
}
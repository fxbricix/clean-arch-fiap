package com.fiap.cleanarchitecture.infrastructure.persistence.mappers;

import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.RestauranteEntity;

public class RestaurantePersistenceMapper {

    public Restaurante toDomainEntity(RestauranteEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Restaurante(
                entity.getId(),
                entity.getNome(),
                entity.getEndereco(),
                entity.getTipoCozinha(),
                new HorarioFuncionamento(entity.getHoraAbertura(), entity.getHoraFechamento()),
                entity.getDonoId());
    }

    public RestauranteEntity toEntity(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }
        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(restaurante.getId());
        entity.setNome(restaurante.getNome());
        entity.setEndereco(restaurante.getEndereco());
        entity.setTipoCozinha(restaurante.getTipoCozinha());
        entity.setHoraAbertura(restaurante.getHorarioFuncionamento().abertura());
        entity.setHoraFechamento(restaurante.getHorarioFuncionamento().fechamento());
        entity.setDonoId(restaurante.getIdDono());
        return entity;
    }
}

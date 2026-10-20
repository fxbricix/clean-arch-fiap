package com.fiap.cleanarchitecture.infrastructure.persistence.mappers;

import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.ItemCardapioEntity;

public class ItemCardapioPersistenceMapper {

    public ItemCardapio toDomainEntity(ItemCardapioEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ItemCardapio(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.isApenasNoLocal(),
                entity.getCaminhoFoto(),
                entity.getRestauranteId());
    }

    public ItemCardapioEntity toEntity(ItemCardapio item) {
        if (item == null) {
            return null;
        }
        ItemCardapioEntity entity = new ItemCardapioEntity();
        entity.setId(item.getId());
        entity.setNome(item.getNome());
        entity.setDescricao(item.getDescricao());
        entity.setPreco(item.getPreco());
        entity.setApenasNoLocal(item.isApenasNoLocal());
        entity.setCaminhoFoto(item.getCaminhoFoto());
        entity.setRestauranteId(item.getIdRestaurante());
        return entity;
    }
}

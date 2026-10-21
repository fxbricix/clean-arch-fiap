package com.fiap.cleanarchitecture.infrastructure.web.mappers;

import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.ItemCardapioDTO;

public class ItemCardapioWebMapper {

    public ItemCardapioDTO toDTO(ItemCardapio item) {
        if (item == null) {
            return null;
        }
        return new ItemCardapioDTO(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.isApenasNoLocal(),
                item.getCaminhoFoto(),
                item.getIdRestaurante());
    }
}

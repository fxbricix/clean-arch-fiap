package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import java.math.BigDecimal;

public record ItemCardapioDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Long restauranteId
) {
}

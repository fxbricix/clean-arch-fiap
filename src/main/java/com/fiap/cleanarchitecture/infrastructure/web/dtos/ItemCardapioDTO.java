package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Item do cardápio")
public record ItemCardapioDTO(
        @Schema(description = "Id do item", example = "1")
        Long id,
        @Schema(description = "Nome do item", example = "Pizza Margherita")
        String nome,
        @Schema(description = "Descrição do item", example = "Molho de tomate, mussarela e manjericão")
        String descricao,
        @Schema(description = "Preço em reais", example = "39.90")
        BigDecimal preco,
        @Schema(description = "Se o item só pode ser consumido no local", example = "false")
        boolean apenasNoLocal,
        @Schema(description = "Caminho da foto do item", example = "/fotos/margherita.jpg")
        String caminhoFoto,
        @Schema(description = "Id do restaurante", example = "1")
        Long restauranteId
) {
}

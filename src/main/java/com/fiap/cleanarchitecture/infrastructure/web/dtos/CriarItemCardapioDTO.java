package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados para criação ou atualização de um item do cardápio")
public record CriarItemCardapioDTO(
        @NotBlank(message = "O nome do item é obrigatório")
        @Size(max = 120, message = "O nome do item deve ter no máximo 120 caracteres")
        @Schema(description = "Nome do item", example = "Pizza Margherita")
        String nome,

        @NotBlank(message = "A descrição do item é obrigatória")
        @Size(max = 500, message = "A descrição do item deve ter no máximo 500 caracteres")
        @Schema(description = "Descrição do item", example = "Molho de tomate, mussarela e manjericão")
        String descricao,

        @NotNull(message = "O preço do item é obrigatório")
        @Positive(message = "O preço do item deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço do item deve ter no máximo 2 casas decimais")
        @Schema(description = "Preço em reais, até 2 casas decimais", example = "39.90")
        BigDecimal preco,

        @NotNull(message = "Informe se o item é disponível apenas no local")
        @Schema(description = "Se verdadeiro, o item só pode ser consumido no local", example = "false")
        Boolean apenasNoLocal,

        @Size(max = 255, message = "O caminho da foto deve ter no máximo 255 caracteres")
        @Schema(description = "Caminho da foto do item (opcional)", example = "/fotos/margherita.jpg")
        String caminhoFoto
) {
}

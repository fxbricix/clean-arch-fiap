package com.fiap.cleanarchitecture.infrastructure.web.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CriarItemCardapioDTO(
        @NotBlank(message = "O nome do item é obrigatório")
        @Size(max = 120, message = "O nome do item deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "A descrição do item é obrigatória")
        @Size(max = 500, message = "A descrição do item deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "O preço do item é obrigatório")
        @Positive(message = "O preço do item deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço do item deve ter no máximo 2 casas decimais")
        BigDecimal preco,

        @NotNull(message = "Informe se o item é disponível apenas no local")
        Boolean apenasNoLocal,

        @Size(max = 255, message = "O caminho da foto deve ter no máximo 255 caracteres")
        String caminhoFoto
) {
}

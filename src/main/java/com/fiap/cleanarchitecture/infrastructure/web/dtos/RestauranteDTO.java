package com.fiap.cleanarchitecture.infrastructure.web.dtos;

public record RestauranteDTO(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        HorarioFuncionamentoDTO horarioFuncionamento,
        Long donoId
) {
}
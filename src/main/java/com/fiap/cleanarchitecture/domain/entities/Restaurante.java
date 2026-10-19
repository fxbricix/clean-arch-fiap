package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import lombok.Getter;

@Getter
public class Restaurante {
    private Long id;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private HorarioFuncionamento horarioFuncionamento;
    private Long idDono;

    private static final int TAMANHO_MAXIMO_NOME = 120;
    private static final int TAMANHO_MAXIMO_ENDERECO = 255;
    private static final int TAMANHO_MAXIMO_TIPO_COZINHA = 50;

    public String validarNome(String nome) {
        if (nome == null || nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new DominioException("Nome inválido");
        }
        return nome;
    }

    public String validarEndereco(String endereco) {
        if (endereco == null || endereco.length() > TAMANHO_MAXIMO_ENDERECO) {
            throw new DominioException("Endereço inválido");
        }
        return endereco;
    }

    public String validarTipoCozinha(String tipoCozinha) {
        if (tipoCozinha == null || tipoCozinha.length() > TAMANHO_MAXIMO_TIPO_COZINHA) {
            throw new DominioException("Tipo de cozinha inválido");
        }
        return tipoCozinha;
    }

    public Restaurante(Long id, String nome, String endereco, String tipoCozinha, HorarioFuncionamento horarioFuncionamento, Long idDono) {
        this.id = id;
        this.nome = validarNome(nome);
        this.endereco = validarEndereco(endereco);
        this.tipoCozinha = validarTipoCozinha(tipoCozinha);
        this.horarioFuncionamento = horarioFuncionamento;
        this.idDono = idDono;
    }
}

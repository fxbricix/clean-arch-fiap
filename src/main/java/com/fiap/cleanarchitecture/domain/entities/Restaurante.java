package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import lombok.Getter;

@Getter
public class Restaurante {

    public static final int TAMANHO_MAXIMO_NOME = 120;
    public static final int TAMANHO_MAXIMO_ENDERECO = 255;
    public static final int TAMANHO_MAXIMO_TIPO_COZINHA = 60;

    private final Long id;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private HorarioFuncionamento horarioFuncionamento;
    private Long idDono;

    public Restaurante(Long id, String nome, String endereco, String tipoCozinha,
                       HorarioFuncionamento horarioFuncionamento, Long idDono) {
        this.id = id;
        this.nome = texto(nome, "O nome", TAMANHO_MAXIMO_NOME);
        this.endereco = texto(endereco, "O endereço", TAMANHO_MAXIMO_ENDERECO);
        this.tipoCozinha = texto(tipoCozinha, "O tipo de cozinha", TAMANHO_MAXIMO_TIPO_COZINHA);
        this.horarioFuncionamento = obrigatorio(horarioFuncionamento, "O horário de funcionamento");
        this.idDono = obrigatorio(idDono, "O dono do restaurante");
    }

    public void alterarDados(String nome, String endereco, String tipoCozinha,
                             HorarioFuncionamento horarioFuncionamento, Long idDono) {
        // valida tudo antes de alterar qualquer campo
        String novoNome = texto(nome, "O nome", TAMANHO_MAXIMO_NOME);
        String novoEndereco = texto(endereco, "O endereço", TAMANHO_MAXIMO_ENDERECO);
        String novoTipoCozinha = texto(tipoCozinha, "O tipo de cozinha", TAMANHO_MAXIMO_TIPO_COZINHA);
        HorarioFuncionamento novoHorario = obrigatorio(horarioFuncionamento, "O horário de funcionamento");
        Long novoDono = obrigatorio(idDono, "O dono do restaurante");

        this.nome = novoNome;
        this.endereco = novoEndereco;
        this.tipoCozinha = novoTipoCozinha;
        this.horarioFuncionamento = novoHorario;
        this.idDono = novoDono;
    }

    private static String texto(String valor, String campo, int tamanhoMaximo) {
        if (valor == null || valor.isBlank()) {
            throw new DominioException(campo + " é obrigatório");
        }
        String normalizado = valor.trim();
        if (normalizado.length() > tamanhoMaximo) {
            throw new DominioException(campo + " deve ter no máximo " + tamanhoMaximo + " caracteres");
        }
        return normalizado;
    }

    private static <T> T obrigatorio(T valor, String campo) {
        if (valor == null) {
            throw new DominioException(campo + " é obrigatório");
        }
        return valor;
    }
}
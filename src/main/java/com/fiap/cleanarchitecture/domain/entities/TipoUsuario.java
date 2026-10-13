package com.fiap.cleanarchitecture.domain.entities;

import lombok.Getter;

@Getter
public class TipoUsuario {
    private static final int TAMANHO_MAXIMO_NOME = 50;
    private static final String DONO_RESTAURANTE = "DONO_RESTAURANTE";

    private final Long id;
    private String nome;

    public TipoUsuario(Long id, String nome) {
        this.id = id;
        this.nome = validar(nome);
    }

    public TipoUsuario(String nome) {
        this(null, nome);
    }

    public void alterarNome(String nome) {
        this.nome = validar(nome);
    }

    public boolean ehDonoDeRestaurante() {
        return DONO_RESTAURANTE.equals(nome);
    }

    private static String validar(String nome) {
        if (nome == null || nome.isBlank() || nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException("Nome do tipo de usuário inválido");
        }
        return nome.trim();
    }
}
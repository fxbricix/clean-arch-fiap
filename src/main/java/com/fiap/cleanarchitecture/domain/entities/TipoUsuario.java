package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import lombok.Getter;

@Getter
public class TipoUsuario {
    private static final int TAMANHO_MAXIMO_NOME = 50;
    public static final String CLIENTE = "CLIENTE";
    public static final String DONO_RESTAURANTE = "DONO_RESTAURANTE";

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

    public boolean ehTipoSistemico() {
        return CLIENTE.equals(nome) || DONO_RESTAURANTE.equals(nome);
    }

    private static String validar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DominioException("O nome do tipo de usuário é obrigatório");
        }
        String normalizado = nome.trim();
        if (normalizado.length() > TAMANHO_MAXIMO_NOME) {
            throw new DominioException("O nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres");
        }
        return normalizado;
    }
}
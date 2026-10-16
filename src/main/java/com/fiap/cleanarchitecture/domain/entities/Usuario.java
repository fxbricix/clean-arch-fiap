package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import lombok.Getter;

@Getter
public class Usuario {

    public static final int TAMANHO_MAXIMO_NOME = 120;

    private final Long id;
    private String nome;
    private final Email email;
    private TipoUsuario tipoUsuario;

    public Usuario(Long id, String nome, Email email, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
        this.tipoUsuario = validarTipo(tipoUsuario);
    }

    public Usuario(String nome, Email email, TipoUsuario tipoUsuario) {
        this(null, nome, email, tipoUsuario);
    }

    public void alterarTipo(TipoUsuario novoTipo) {
        this.tipoUsuario = validarTipo(novoTipo);
    }

    public boolean ehDonoDeRestaurante() {
        return tipoUsuario.ehDonoDeRestaurante();
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DominioException("O nome do usuário é obrigatório");
        }
        String normalizado = nome.trim();
        if (normalizado.length() > TAMANHO_MAXIMO_NOME) {
            throw new DominioException("O nome do usuário deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres");
        }
        return normalizado;
    }

    private static Email validarEmail(Email email) {
        if (email == null) {
            throw new DominioException("O e-mail do usuário é obrigatório");
        }
        return email;
    }

    private static TipoUsuario validarTipo(TipoUsuario tipo) {
        if (tipo == null) {
            throw new DominioException("O tipo de usuário é obrigatório");
        }
        return tipo;
    }
}
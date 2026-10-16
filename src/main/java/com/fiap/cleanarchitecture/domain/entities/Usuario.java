package com.fiap.cleanarchitecture.domain.entities;

import lombok.Getter;

@Getter
public class Usuario {
    private String nome;
    private Email email;
    private TipoUsuario tipoUsuario;

    public Usuario(String nome, Email email, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

}

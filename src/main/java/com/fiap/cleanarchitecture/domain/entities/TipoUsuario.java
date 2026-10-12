package com.fiap.cleanarchitecture.domain.entities;

import lombok.Getter;

@Getter
public class TipoUsuario {
    private String nome;

    public TipoUsuario(String nome) {
        validaNome(nome);
        this.nome = nome;
    }

    public void setNome(String nome) {
        validaNome(nome);
        this.nome = nome;
    }

    public void validaNome(String nome){
        if (nome == null || nome.length() > 50) {
            throw new IllegalArgumentException("Nome inválido");
        }
    }
}
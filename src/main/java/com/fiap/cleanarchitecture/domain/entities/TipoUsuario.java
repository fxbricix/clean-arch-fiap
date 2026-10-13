package com.fiap.cleanarchitecture.domain.entities;

import lombok.Getter;

@Getter
public class TipoUsuario {
    private String tipo;

    public TipoUsuario(String tipo) {
        validaTipo(tipo);
        this.tipo = tipo;
    }

    public void setTipo(String tipo) {
        validaTipo(tipo);
        this.tipo = tipo;
    }

    public void validaTipo(String tipo){
        if (tipo == null || tipo.length() > 50) {
            throw new IllegalArgumentException("Tipo de usuario inválido");
        }
    }
}
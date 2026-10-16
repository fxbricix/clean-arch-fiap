package com.fiap.cleanarchitecture.domain.valueobjects;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String valor) {

    public static final int TAMANHO_MAXIMO = 160;
    private static final Pattern FORMATO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (valor == null || valor.isBlank()) {
            throw new DominioException("O e-mail é obrigatório");
        }
        valor = valor.trim().toLowerCase(Locale.ROOT);
        if (valor.length() > TAMANHO_MAXIMO) {
            throw new DominioException("O e-mail deve ter no máximo " + TAMANHO_MAXIMO + " caracteres");
        }
        if (!FORMATO.matcher(valor).matches()) {
            throw new DominioException("O e-mail informado é inválido");
        }
    }
}
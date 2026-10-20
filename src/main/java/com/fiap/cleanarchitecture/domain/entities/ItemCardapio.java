package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ItemCardapio {

    public static final int TAMANHO_MAXIMO_NOME = 120;
    public static final int TAMANHO_MAXIMO_DESCRICAO = 500;
    public static final int TAMANHO_MAXIMO_CAMINHO_FOTO = 255;
    public static final int CASAS_DECIMAIS_PRECO = 2;

    private final Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean apenasNoLocal;
    private String caminhoFoto;
    private final Long idRestaurante;

    public ItemCardapio(Long id, String nome, String descricao, BigDecimal preco,
                        boolean apenasNoLocal, String caminhoFoto, Long idRestaurante) {
        this.id = id;
        this.nome = validarNome(nome);
        this.descricao = validarDescricao(descricao);
        this.preco = validarPreco(preco);
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = validarCaminhoFoto(caminhoFoto);
        if (idRestaurante == null) {
            throw new DominioException("O restaurante do item do cardápio é obrigatório");
        }
        this.idRestaurante = idRestaurante;
    }

    public void alterarDados(String nome, String descricao, BigDecimal preco,
                             boolean apenasNoLocal, String caminhoFoto) {
        String novoNome = validarNome(nome);
        String novaDescricao = validarDescricao(descricao);
        BigDecimal novoPreco = validarPreco(preco);
        String novoCaminhoFoto = validarCaminhoFoto(caminhoFoto);

        this.nome = novoNome;
        this.descricao = novaDescricao;
        this.preco = novoPreco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = novoCaminhoFoto;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DominioException("O nome do item do cardápio é obrigatório");
        }
        String normalizado = nome.trim();
        if (normalizado.length() > TAMANHO_MAXIMO_NOME) {
            throw new DominioException("O nome do item do cardápio deve ter no máximo "
                    + TAMANHO_MAXIMO_NOME + " caracteres");
        }
        return normalizado;
    }

    private static String validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new DominioException("A descrição do item do cardápio é obrigatória");
        }
        String normalizado = descricao.trim();
        if (normalizado.length() > TAMANHO_MAXIMO_DESCRICAO) {
            throw new DominioException("A descrição do item do cardápio deve ter no máximo "
                    + TAMANHO_MAXIMO_DESCRICAO + " caracteres");
        }
        return normalizado;
    }

    private static BigDecimal validarPreco(BigDecimal preco) {
        if (preco == null || preco.signum() <= 0) {
            throw new DominioException("O preço do item do cardápio deve ser maior que zero");
        }
        if (preco.stripTrailingZeros().scale() > CASAS_DECIMAIS_PRECO) {
            throw new DominioException("O preço do item do cardápio deve ter no máximo "
                    + CASAS_DECIMAIS_PRECO + " casas decimais");
        }
        return preco;
    }

    private static String validarCaminhoFoto(String caminhoFoto) {
        if (caminhoFoto == null || caminhoFoto.isBlank()) {
            return null;
        }
        String normalizado = caminhoFoto.trim();
        if (normalizado.length() > TAMANHO_MAXIMO_CAMINHO_FOTO) {
            throw new DominioException("O caminho da foto deve ter no máximo "
                    + TAMANHO_MAXIMO_CAMINHO_FOTO + " caracteres");
        }
        return normalizado;
    }
}
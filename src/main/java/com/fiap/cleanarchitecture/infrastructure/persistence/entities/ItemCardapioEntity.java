package com.fiap.cleanarchitecture.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_cardapio")
@Getter
@Setter
@NoArgsConstructor
public class ItemCardapioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "apenas_no_local", nullable = false)
    private boolean apenasNoLocal;

    @Column(name = "caminho_foto", length = 255)
    private String caminhoFoto;

    @Column(name = "restaurante_id", nullable = false)
    private Long restauranteId;
}

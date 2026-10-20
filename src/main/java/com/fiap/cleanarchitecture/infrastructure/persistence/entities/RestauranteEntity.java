package com.fiap.cleanarchitecture.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "restaurante")
@Getter
@Setter
@NoArgsConstructor
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Column(name = "tipo_cozinha", nullable = false, length = 60)
    private String tipoCozinha;

    @Column(name = "hora_abertura", nullable = false)
    private LocalTime horaAbertura;

    @Column(name = "hora_fechamento", nullable = false)
    private LocalTime horaFechamento;

    @Column(name = "dono_id", nullable = false)
    private Long donoId;
}

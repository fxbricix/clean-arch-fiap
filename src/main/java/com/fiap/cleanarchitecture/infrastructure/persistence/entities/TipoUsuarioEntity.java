package com.fiap.cleanarchitecture.infrastructure.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tipo_usuario")
@Data
public class TipoUsuarioEntity {
    private long id;
    private String nome;
}

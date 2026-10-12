CREATE TABLE restaurante (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(120) NOT NULL,
    endereco         VARCHAR(255) NOT NULL,
    tipo_cozinha     VARCHAR(60)  NOT NULL,
    hora_abertura    TIME NOT NULL,
    hora_fechamento  TIME NOT NULL,
    dono_id          BIGINT NOT NULL REFERENCES usuarios(id)
);
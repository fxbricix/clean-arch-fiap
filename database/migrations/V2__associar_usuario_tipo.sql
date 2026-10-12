CREATE TABLE usuarios (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    email           VARCHAR(160) NOT NULL UNIQUE,
    tipo_usuario_id BIGINT NOT NULL REFERENCES tipo_usuario(id)
);
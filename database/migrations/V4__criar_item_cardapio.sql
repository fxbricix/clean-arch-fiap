CREATE TABLE item_cardapio (
    id              BIGSERIAL PRIMARY KEY,
    restaurante_id  BIGINT NOT NULL REFERENCES restaurante(id),
    nome            VARCHAR(120) NOT NULL,
    descricao       VARCHAR(500),
    preco           NUMERIC(10,2) NOT NULL,
    apenas_no_local BOOLEAN NOT NULL DEFAULT FALSE,
    caminho_foto    VARCHAR(255)
);
ALTER TABLE item_cardapio ALTER COLUMN descricao SET NOT NULL;
ALTER TABLE item_cardapio ADD CONSTRAINT ck_item_cardapio_preco_positivo CHECK (preco > 0);

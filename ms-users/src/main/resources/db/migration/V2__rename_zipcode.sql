-- Migration V2: Renomear a coluna "zipcode" para "zipcode"
-- Renomear a coluna 'zipcode' para 'zipcode' na tabela address

-- Renomeando a coluna 'zipcode' para 'zipcode'
ALTER TABLE address
RENAME COLUMN cep TO zipcode;

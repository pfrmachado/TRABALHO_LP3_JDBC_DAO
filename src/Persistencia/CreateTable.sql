CREATE TABLE estoque
(
    filial VARCHAR(150) NOT NULL,
    produto VARCHAR(150) NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY(filial, produto)
);

CREATE TABLE IF NOT EXISTS contas (
    id BIGSERIAL PRIMARY KEY,
    numero VARCHAR(20) NOT NULL UNIQUE,
    titular VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    saldo NUMERIC(15, 2) NOT NULL DEFAULT 0,

    CONSTRAINT chk_contas_tipo
        CHECK (tipo IN ('CORRENTE', 'POUPANCA')),

    CONSTRAINT chk_contas_saldo
        CHECK (saldo >= 0),

    CONSTRAINT chk_contas_titular
        CHECK (titular <> '')
);

CREATE TABLE IF NOT EXISTS transacoes (
    id BIGSERIAL PRIMARY KEY,

    identificador UUID NOT NULL UNIQUE,

    conta_id BIGINT NOT NULL,

    tipo VARCHAR(30) NOT NULL,

    valor NUMERIC(15, 2) NOT NULL,

    data_hora TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transacoes_conta
        FOREIGN KEY (conta_id)
        REFERENCES contas(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_transacoes_tipo
        CHECK (
            tipo IN (
                'DEPOSITO',
                'SAQUE',
                'TRANSFERENCIA',
                'RENDIMENTO'
            )
        ),

    CONSTRAINT chk_transacoes_valor
        CHECK (valor > 0)
);
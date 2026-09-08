INSERT INTO contas (
    numero,
    titular,
    tipo,
    saldo
)
VALUES
    ('001', 'Arthur', 'CORRENTE', 300),
    ('002', 'Maria', 'POUPANCA', 1400),
    ('003', 'Carlos', 'CORRENTE', 100)
ON CONFLICT (numero) DO NOTHING;
package br.com.arthur.banco;

import java.time.LocalDateTime;

public class Transacao {
    private final TipoTransacao tipo;
    private final double valor;
    private final LocalDateTime dataHora;

    public Transacao(TipoTransacao tipo, double valor) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de transação não pode ser nulo.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor da transação deve ser positivo.");
        }
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
}

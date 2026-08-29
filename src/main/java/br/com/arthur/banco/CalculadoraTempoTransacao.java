package br.com.arthur.banco;

import java.time.Duration;

public class CalculadoraTempoTransacao {

    public long calcularMinutosEntreTransacoes(Transacao transacao1, Transacao transacao2) {
        if (transacao1 == null || transacao2 == null) {
            throw new IllegalArgumentException("Transações não podem ser nulas.");
        }

        Duration duracao = Duration.between(
            transacao1.getDataHora(),
            transacao2.getDataHora()
        );
        return Math.abs(duracao.toMinutes());
    }
}


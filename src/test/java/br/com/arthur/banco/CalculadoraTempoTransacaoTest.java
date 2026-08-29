package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class CalculadoraTempoTransacaoTest {
    @Test
    void deveCalcularMinutosEntreDuasTransacoes() {
        LocalDateTime primeiraData =
            LocalDateTime.of(2026, 8, 29, 10, 0);

        LocalDateTime segundaData =
            LocalDateTime.of(2026, 8, 29, 12, 30);

        Transacao primeira = new Transacao(
            TipoTransacao.DEPOSITO,
            100.0,
            primeiraData
        );

        Transacao segunda = new Transacao(
            TipoTransacao.SAQUE,
            50.0,
            segundaData
        );

        CalculadoraTempoTransacao calculadora =
            new CalculadoraTempoTransacao();

        long minutos =
            calculadora.calcularMinutosEntreTransacoes(
                primeira,
                segunda
            );

        assertEquals(150, minutos);
    }

    @Test
    void deveCalcularMinutosMesmoComTransacoesEmOrdemInvertida() {
        LocalDateTime primeiraData =
            LocalDateTime.of(2026, 8, 29, 12, 30);

        LocalDateTime segundaData =
            LocalDateTime.of(2026, 8, 29, 10, 0);

        Transacao primeira = new Transacao(
            TipoTransacao.DEPOSITO,
            100.0,
            primeiraData
        );

        Transacao segunda = new Transacao(
            TipoTransacao.SAQUE,
            50.0,
            segundaData
        );

        CalculadoraTempoTransacao calculadora =
            new CalculadoraTempoTransacao();

        long minutos =
            calculadora.calcularMinutosEntreTransacoes(
                primeira,
                segunda
            );

        assertEquals(150, minutos);
    }

    
}

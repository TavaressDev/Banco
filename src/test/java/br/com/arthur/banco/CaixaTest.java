package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CaixaTest {

    @Test
    void deveArmazenarStringNaCaixa() {
        Caixa<String> caixa = new Caixa<>("Arthur");

        assertEquals("Arthur", caixa.getValor());
    }

    @Test
    void deveArmazenarIntegerNaCaixa() {
        Caixa<Integer> caixa = new Caixa<>(100);

        assertEquals(100, caixa.getValor());
    }
}
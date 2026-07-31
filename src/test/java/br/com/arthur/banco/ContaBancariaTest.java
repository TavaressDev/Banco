package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ContaBancariaTest {

    @Test
    void contasComMesmoNumeroDevemSerIguais() {
        ContaBancaria primeiraConta =
            new ContaCorrente("Carlos", "001");

        ContaBancaria segundaConta =
            new ContaPoupanca("Maria", "001");

        assertEquals(primeiraConta, segundaConta);
    }

    @Test
    void contasComNumerosDiferentesNaoDevemSerIguais() {
        ContaBancaria primeiraConta =
            new ContaCorrente("Carlos", "001");

        ContaBancaria segundaConta =
            new ContaCorrente("Carlos", "002");

        assertNotEquals(primeiraConta, segundaConta);
    }

    @Test
    void contasIguaisDevemTerMesmoHashCode() {
        ContaBancaria primeiraConta =
            new ContaCorrente("Carlos", "001");

        ContaBancaria segundaConta =
            new ContaPoupanca("Maria", "001");

        assertEquals(
            primeiraConta.hashCode(),
            segundaConta.hashCode()
        );
    }


    
}
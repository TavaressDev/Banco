package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    void setNaoDeveAceitarContasComMesmoNumero() {
        Set<ContaBancaria> contas = new HashSet<>();

        ContaBancaria primeiraConta =
            new ContaCorrente("Carlos", "001");

        ContaBancaria segundaConta =
            new ContaPoupanca("Maria", "001");

        contas.add(primeiraConta);
        contas.add(segundaConta);

        assertEquals(1, contas.size());
    }

    @Test
    void devePermitirContasComNumerosDiferentesNoSet() {
        Set<ContaBancaria> contas = new HashSet<>();

        ContaBancaria conta1 =
            new ContaCorrente("Carlos", "001");

        ContaBancaria conta2 =
            new ContaCorrente("Carlos", "002");

        contas.add(conta1);
        contas.add(conta2);

        assertEquals(2, contas.size());
    }

    @Test
    void adicionarObjetoDuplicadoNoSetDeveRetornarFalse() {
        Set<ContaBancaria> contas = new HashSet<>();

        ContaBancaria conta1 =
            new ContaCorrente("Carlos", "001");

        ContaBancaria conta2 =
            new ContaPoupanca("Maria", "001");

        boolean primeiraAdicao = contas.add(conta1);
        boolean segundaAdicao = contas.add(conta2);

        assertTrue(primeiraAdicao);
        assertFalse(segundaAdicao);
    }
}
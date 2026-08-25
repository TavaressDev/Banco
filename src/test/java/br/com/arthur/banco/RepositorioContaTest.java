package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class RepositorioContaTest {

    @Test
    void deveAceitarContaCorrente() {
        ContaCorrente conta =
            new ContaCorrente("Carlos", "001");

        RepositorioConta<ContaCorrente> repositorio =
            new RepositorioConta<>(conta);

        assertSame(conta, repositorio.getConta());
        assertEquals("001", repositorio.obterNumeroDaConta());
    }

    @Test
    void deveAceitarContaPoupanca() {
        ContaPoupanca conta =
            new ContaPoupanca("Maria", "002");

        RepositorioConta<ContaPoupanca> repositorio =
            new RepositorioConta<>(conta);

        assertSame(conta, repositorio.getConta());
        assertEquals("002", repositorio.obterNumeroDaConta());
    }
}
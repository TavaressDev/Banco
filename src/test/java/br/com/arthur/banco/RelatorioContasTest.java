package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class RelatorioContasTest {

    @Test
    void deveCalcularSaldoDeContasCorrentes() {
        ContaCorrente conta1 =
            new ContaCorrente("Carlos", "001");

        ContaCorrente conta2 =
            new ContaCorrente("Maria", "002");

        conta1.depositar(100.0);
        conta2.depositar(200.0);

        List<ContaCorrente> contas =
            List.of(conta1, conta2);

        RelatorioContas relatorio =
            new RelatorioContas();

        assertEquals(
            300.0,
            relatorio.calcularSaldoTotal(contas),
            0.001
        );
    }

    @Test
    void deveAdicionarContaCorrenteEmListaDeContasBancarias() {
        List<ContaBancaria> contas = new ArrayList<>();

        ContaCorrente conta =
            new ContaCorrente("Carlos", "001");

        RelatorioContas relatorio =
            new RelatorioContas();

        relatorio.adicionarContaCorrente(contas, conta);

        assertEquals(1, contas.size());
        assertSame(conta, contas.get(0));
    }
}
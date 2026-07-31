package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ContaCorrenteTest {
    
    @Test
    void deveCobrarTarifaAoRealizarSaque() {
        ContaCorrente conta = new ContaCorrente("Carlos", "003");
        conta.depositar(100.0);

        conta.sacar(40.0);

        assertEquals(58.0, conta.getSaldo(), 0.001);
    }

    @Test
    void deveLancarExcecaoQuandoSaldoNaoCobrirSaqueETarifa() {
        ContaCorrente conta = new ContaCorrente("Ana", "004");
        conta.depositar(50.0);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> conta.sacar(50.0)
        );
        
        assertEquals(
            "Saldo insuficiente para realizar o saque.",
            exception.getMessage()
        );
    }

    @Test
    void deveRegistrarSaqueNoHistorico() {
        ContaCorrente conta = new ContaCorrente("Carlos", "003");
        conta.depositar(100.0);

        conta.sacar(40.0);

        List<Transacao> historico = conta.getTransacaosHistorico();

        assertAll(
            () -> assertEquals(2, historico.size()),
            () -> assertEquals(
                TipoTransacao.SAQUE,
                historico.get(1).getTipo()
            ),
            () -> assertEquals(
                40.0,
                historico.get(1).getValor(),
                0.001
            )
        );
    }
}

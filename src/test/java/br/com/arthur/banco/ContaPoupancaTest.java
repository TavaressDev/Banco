package br.com.arthur.banco;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContaPoupancaTest {

    @Test
    void deveDepositarValorNaConta() {
        ContaPoupanca conta = new ContaPoupanca("João", "001");

        conta.depositar(100.0);

        assertEquals(100.0, conta.getSaldo(), 0.001);
    }

    @Test
    void deveLancarExcecaoAoDepositarValorInvalido() {
        ContaPoupanca conta = new ContaPoupanca("Maria", "002");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> conta.depositar(0.0)
        );

        assertEquals(
            "O valor do depósito deve ser maior que zero.",
            exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -100.0})
    void deveLancarExcecaoParaDiferentesDepositosInvalidos(double valor){
        ContaPoupanca conta = new ContaPoupanca("Maria", "002");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> conta.depositar(valor)
        );

        assertEquals(
            "O valor do depósito deve ser maior que zero.",
            exception.getMessage()
        );
    }

    @Test
    void deveRegistrarDepositoNoHistorico() {
        ContaPoupanca conta = new ContaPoupanca("Maria", "001");

        conta.depositar(100.0);

        List<Transacao> historico = conta.getTransacaosHistorico();

        assertAll(
            () -> assertEquals(1, historico.size()),
            () -> assertEquals(
                TipoTransacao.DEPOSITO,
                historico.get(0).getTipo()
            ),
            () -> assertEquals(
                100.0,
                historico.get(0).getValor(),
                0.001
            )
        );
    }

}

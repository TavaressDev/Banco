package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TransacaoTest {

    @Test
    void deveCriarTransacaoValida() {
        // Arrange + Act
        Transacao transacao =
            new Transacao(TipoTransacao.DEPOSITO, 100.0);

        // Assert
        assertEquals(TipoTransacao.DEPOSITO, transacao.getTipo());
        assertEquals(100.0, transacao.getValor(), 0.001);
        assertNotNull(transacao.getDataHora());
    }

    @Test
    void deveLancarExcecaoQuandoTipoForNulo() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transacao(null, 100.0)
        );

        assertEquals(
            "Tipo de transação não pode ser nulo.",
            exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -100.0})
    void deveLancarExcecaoQuandoValorForInvalido(double valorInvalido) {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Transacao(
                TipoTransacao.SAQUE,
                valorInvalido
            )
        );

        assertEquals(
            "Valor da transação deve ser positivo.",
            exception.getMessage()
        );
    }
}
package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ContaResumoTest {
    @Test
    void deveLancarExcecaoQuandoNumeroForInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ContaResumo(
                "",
                "Maria",
                100.0
            )
        );

        assertEquals(
            "O número não pode ser nulo ou vazio.",
            exception.getMessage()
        );
    }

    @Test
    void deveLancarExcecaoQuandoTitularForInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ContaResumo(
                "123",
                "",
                100.0
            )
        );

        assertEquals(
            "O titular não pode ser nulo ou vazio.",
            exception.getMessage()
        );
    }   

    @Test
    void deveLancarExcecaoQuandoSaldoForNegativo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ContaResumo(
                "123",
                "Maria",
                -100.0
            )
        );

        assertEquals(
            "O saldo não pode ser negativo.",
            exception.getMessage()
        );
    }
}

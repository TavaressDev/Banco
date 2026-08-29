package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Period;

import org.junit.jupiter.api.Test;

public class CalculadoraPeriodoTest {
    @Test
    void deveCalcularPeriodoEntreDuasDatas() {
        LocalDate inicio =
            LocalDate.of(2020, 1, 1);

        LocalDate fim =
            LocalDate.of(2026, 8, 29);

        CalculadoraPeriodo calculadora =
            new CalculadoraPeriodo();

        Period periodo =
            calculadora.calcularPeriodoEntre(
                inicio,
                fim
            );

        assertEquals(6, periodo.getYears());
        assertEquals(7, periodo.getMonths());
        assertEquals(28, periodo.getDays());
    }
}

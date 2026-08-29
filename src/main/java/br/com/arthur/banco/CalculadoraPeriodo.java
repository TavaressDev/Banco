package br.com.arthur.banco;

import java.time.LocalDate;
import java.time.Period;

public class CalculadoraPeriodo {

    public Period calcularPeriodoEntre(
        LocalDate inicio,
        LocalDate fim
    ) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException(
                "As datas não podem ser nulas."
            );
        }

        return Period.between(inicio, fim);
    }
}
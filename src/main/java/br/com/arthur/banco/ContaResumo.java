package br.com.arthur.banco;

public record ContaResumo(
    String numero,
    String titular,
    double saldo
) {

    public ContaResumo {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                "O número não pode ser nulo ou vazio."
            );
        }

        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException(
                "O titular não pode ser nulo ou vazio."
            );
        }

        if (saldo < 0) {
            throw new IllegalArgumentException(
                "O saldo não pode ser negativo."
            );
        }
    }
}

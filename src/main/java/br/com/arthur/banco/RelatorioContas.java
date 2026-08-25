package br.com.arthur.banco;

import java.util.List;

public class RelatorioContas {

    public double calcularSaldoTotal(
        List<? extends ContaBancaria> contas
    ) {
        double total = 0.0;

        for (ContaBancaria conta : contas) {
            total += conta.getSaldo();
        }

        return total;
    }
}
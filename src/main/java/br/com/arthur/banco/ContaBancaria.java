package br.com.arthur.banco;

import java.util.ArrayList;
import java.util.List;

public abstract class ContaBancaria {
    private final String titular;
    private final String numero;
    private double saldo;
    private final List<Transacao> transacaosHistorico;


    public ContaBancaria(String titular, String numero) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException(
                "O titular não pode ser nulo ou vazio."
            );
        }

        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                "O número da conta não pode ser nulo ou vazio."
            );
        }

        this.titular = titular;
        this.numero = numero;
        this.saldo = 0.0;
        this.transacaosHistorico = new ArrayList<>();
    }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                "O valor do depósito deve ser maior que zero."
            );
        }

        saldo += valor;
        transacaosHistorico.add(
            new Transacao(TipoTransacao.DEPOSITO, valor)
        );    
    }

    public List<Transacao> getTransacaosHistorico() {
        return List.copyOf(transacaosHistorico);
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                "O valor do saque deve ser maior que zero."
            );
        }

        double valorComTaxa = valor + obterTarifaSaque();

        if (valorComTaxa > saldo) {
            throw new IllegalArgumentException(
                "Saldo insuficiente para realizar o saque."
            );
        }

        saldo -= valorComTaxa;
    }

    public void transferir(ContaBancaria contaDestino, double valor) {
        if (contaDestino == null) {
            throw new IllegalArgumentException(
                "A conta de destino não pode ser nula."
            );
        }

        if (contaDestino == this) {
            throw new IllegalArgumentException(
                "Não é possível transferir para a mesma conta."
            );
        }

        this.sacar(valor);
        contaDestino.depositar(valor);
    }

    protected abstract double obterTarifaSaque();

}

package br.com.arthur.banco;

import java.util.ArrayList;
import java.util.List;

public abstract class ContaBancaria {
    private final String titular;
    private final String numero;
    private double saldo;
    private final List<Transacao> transacaosHistorico;

    public ContaBancaria(String titular, String numero) {
        this(titular, numero, 0.0);
    }

    protected ContaBancaria(
            String titular,
            String numero,
            double saldo) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException(
                    "O titular não pode ser nulo ou vazio.");
        }

        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                    "O número da conta não pode ser nulo ou vazio.");
        }

        if (saldo < 0) {
            throw new IllegalArgumentException(
                    "O saldo não pode ser negativo.");
        }

        this.titular = titular;
        this.numero = numero;
        this.saldo = saldo;
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
                    "O valor do depósito deve ser maior que zero.");
        }

        creditar(valor, TipoTransacao.DEPOSITO);
    }

    public List<Transacao> getTransacaosHistorico() {
        return List.copyOf(transacaosHistorico);
    }

    public void sacar(double valor) {
        debitar(valor, TipoTransacao.SAQUE);
    }

    public void transferir(
            ContaBancaria contaDestino,
            double valor) {
        if (contaDestino == null) {
            throw new IllegalArgumentException(
                    "A conta de destino não pode ser nula.");
        }

        if (contaDestino == this) {
            throw new IllegalArgumentException(
                    "Não é possível transferir para a mesma conta.");
        }

        debitar(
                valor,
                TipoTransacao.TRANSFERENCIA);

        contaDestino.creditar(
                valor,
                TipoTransacao.TRANSFERENCIA);
    }

    protected abstract double obterTarifaSaque();

    protected void creditar(
            double valor,
            TipoTransacao tipoTransacao) {
        saldo += valor;

        transacaosHistorico.add(
                new Transacao(
                        tipoTransacao,
                        valor));
    }

    protected void debitar(
            double valor,
            TipoTransacao tipoTransacao) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "O valor do saque deve ser maior que zero.");
        }

        double valorComTaxa = valor + obterTarifaSaque();

        if (valorComTaxa > saldo) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente para realizar o saque.");
        }

        saldo -= valorComTaxa;

        transacaosHistorico.add(
                new Transacao(
                        tipoTransacao,
                        valor));
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof ContaBancaria outraConta)) {
            return false;
        }

        return numero.equals(outraConta.numero);
    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }
}
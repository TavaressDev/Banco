package br.com.arthur.banco;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    private final List<ContaBancaria> contas;

    public Banco() {
        this.contas = new ArrayList<>();    
    }

    public void adicionarConta(ContaBancaria conta) {
        if (conta == null) {
            throw new IllegalArgumentException(
                "A conta não pode ser nula."
            );
        }

        for (ContaBancaria c : contas) {
            if (c.getNumero().equals(conta.getNumero())) {
                throw new IllegalArgumentException(
                    "Já existe uma conta com o mesmo número."
                );
            }
        }

        contas.add(conta);
    }

    public ContaBancaria buscarContaPorNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                "O número da conta não pode ser nulo ou vazio."
            );
        }

        for (ContaBancaria c : contas) {
            if (c.getNumero().equals(numero)) {
                return c;
            }
        }

        throw new ContaNaoEncontradaException(
            "Conta com número " + numero + " não encontrada."
        );
    }

    public void transferir(String numeroOrigem, String numeroDestino, double valor){
        ContaBancaria contaOrigem = buscarContaPorNumero(numeroOrigem);
        ContaBancaria contaDestino = buscarContaPorNumero(numeroDestino);

        contaOrigem.transferir(contaDestino, valor);
    }

    public void sacar(String numero, double valor){
        ContaBancaria conta = buscarContaPorNumero(numero);
        conta.sacar(valor);
    }

    public void aplicarRendimento(String numero, double percentual){
        ContaBancaria conta = buscarContaPorNumero(numero);

        if (!(conta instanceof Rendivel rendivel)) {
            throw new ContaNaoRendivelException(
                "A conta não é rendível."
            );
        }

        rendivel.aplicarRendimento(percentual);
    }

}

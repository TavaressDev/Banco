package br.com.arthur.banco;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Banco {
    private final Map<String, ContaBancaria> contas;

    public Banco() {
        this.contas = new HashMap<>();    
    }

    public void adicionarConta(ContaBancaria conta) {
        if (conta == null) {
            throw new IllegalArgumentException(
                "A conta não pode ser nula."
            );
        }

        if(contas.containsKey(conta.getNumero())){
            throw new IllegalArgumentException(
                "Já existe uma conta com o mesmo número."
            );
        }

        contas.put(conta.getNumero(), conta);
    }

    public ContaBancaria buscarContaPorNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                "O número da conta não pode ser nulo ou vazio."
            );
        }

        ContaBancaria conta = contas.get(numero);

        if (conta == null) { 
            throw new ContaNaoEncontradaException(
                "Conta com número " + numero + " não encontrada."
            );
        }
        
        return conta;
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

    public List<ContaBancaria> listarContas() {
        return List.copyOf(contas.values());
    }

}

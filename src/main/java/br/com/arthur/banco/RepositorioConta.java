package br.com.arthur.banco;

public class RepositorioConta<T extends ContaBancaria> {

    private T conta;

    public RepositorioConta(T conta) {
        this.conta = conta;
    }

    public T getConta() {
        return conta;
    }

    public String obterNumeroDaConta() {
        return conta.getNumero();
    }
}
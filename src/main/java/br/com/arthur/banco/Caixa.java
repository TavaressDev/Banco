package br.com.arthur.banco;

public class Caixa<T> {
    private T valor;

    public Caixa(T valor) {
        this.valor = valor;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }
}
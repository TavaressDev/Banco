package br.com.arthur.banco;

public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException(String message) {
        super(message);
    }

}

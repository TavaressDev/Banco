package br.com.arthur.banco;

public class ContaJaExisteException
        extends RuntimeException {

    public ContaJaExisteException(String message) {
        super(message);
    }
}
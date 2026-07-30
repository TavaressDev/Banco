package br.com.arthur.banco;

public class ContaNaoRendivelException extends RuntimeException {
    public ContaNaoRendivelException(String message) {
        super(message);
    }

}

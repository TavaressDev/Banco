package br.com.arthur.banco;

public record RespostaHttp<T>(
        int status,
        T corpo,
        String erro) {
}
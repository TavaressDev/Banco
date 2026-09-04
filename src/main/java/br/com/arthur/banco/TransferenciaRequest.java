package br.com.arthur.banco;

public record TransferenciaRequest(
        String contaOrigem,
        String contaDestino,
        double valor) {
}
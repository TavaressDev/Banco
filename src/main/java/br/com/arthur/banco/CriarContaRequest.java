package br.com.arthur.banco;

public record CriarContaRequest(
        String titular,
        String numero,
        TipoConta tipo) {
}
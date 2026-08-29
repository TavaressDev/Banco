package br.com.arthur.banco;

public class ExportacaoException extends RuntimeException {
    public ExportacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

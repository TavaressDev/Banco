package br.com.arthur.banco;

public class ContaCorrente extends ContaBancaria {

    public ContaCorrente(
            String titular,
            String numero) {
        super(titular, numero);
    }

    private ContaCorrente(
            String titular,
            String numero,
            double saldo) {
        super(titular, numero, saldo);
    }

    static ContaCorrente reidratar(
            String titular,
            String numero,
            double saldo) {
        return new ContaCorrente(
                titular,
                numero,
                saldo);
    }

    @Override
    protected double obterTarifaSaque() {
        return 2.0;
    }
}
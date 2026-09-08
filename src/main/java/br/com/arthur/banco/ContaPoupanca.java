package br.com.arthur.banco;

public class ContaPoupanca
        extends ContaBancaria
        implements Rendivel {

    public ContaPoupanca(
            String titular,
            String numero) {
        super(titular, numero);
    }

    private ContaPoupanca(
            String titular,
            String numero,
            double saldo) {
        super(titular, numero, saldo);
    }

    static ContaPoupanca reidratar(
            String titular,
            String numero,
            double saldo) {
        return new ContaPoupanca(
                titular,
                numero,
                saldo);
    }

    @Override
    public void aplicarRendimento(
            double percentual) {
        if (percentual <= 0) {
            throw new IllegalArgumentException(
                    "O percentual de rendimento deve ser maior que zero.");
        }

        if (getSaldo() <= 0) {
            throw new IllegalArgumentException(
                    "Não é possível aplicar rendimento em uma conta com saldo zero ou negativo.");
        }

        double rendimento = getSaldo() * (percentual / 100);

        creditar(
                rendimento,
                TipoTransacao.RENDIMENTO);
    }

    @Override
    protected double obterTarifaSaque() {
        return 0.0;
    }
}
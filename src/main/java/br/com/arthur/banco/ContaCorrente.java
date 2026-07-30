package br.com.arthur.banco;

public class ContaCorrente extends ContaBancaria {

    public ContaCorrente(String titular, String numero) {
        super(titular, numero);
    }

    @Override
    protected double obterTarifaSaque() {
         return 2.0;
    }




}

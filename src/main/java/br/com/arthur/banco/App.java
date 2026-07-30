package br.com.arthur.banco;

public class App {
    public static void main(String[] args)  {
        Banco caixa = new Banco();
        try {
    caixa.buscarContaPorNumero("999");
} catch (ContaNaoEncontradaException e) {
    System.out.println("Erro na busca: " + e.getMessage());
}

System.out.println("Aplicação continua funcionando.");

        ContaBancaria contaCorrente =
            new ContaCorrente("Arthur", "001");

        ContaBancaria contaPoupanca =
            new ContaPoupanca("Maria", "002");
        caixa.adicionarConta(contaCorrente);
        caixa.adicionarConta(contaPoupanca);

        contaCorrente.depositar(200);
        contaPoupanca.depositar(200);

        caixa.sacar("001", 100);
        caixa.sacar("002", 100);
        System.out.println("Saldo da conta 1: " + contaCorrente.getSaldo());
        System.out.println("Saldo da conta 2: " + contaPoupanca.getSaldo());

        caixa.aplicarRendimento("002", 2);
        System.out.println("Saldo da conta 2 após rendimento: " + contaPoupanca.getSaldo());

        try {
            caixa.aplicarRendimento("001", 2);
        } catch (ContaNaoRendivelException e) {
            System.out.println("Erro ao aplicar rendimento na conta 1: " + e.getMessage());

        }
        System.out.println("Programa finalizado.");
    }

    
}

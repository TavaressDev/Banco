package br.com.arthur.banco;

public class App {

    public static void main(String[] args) {

        ContaRepository contaRepository = new ContaRepositoryEmMemoria();

        ContaService contaService = new ContaService(contaRepository);

        try {
            contaService.buscarContaPorNumero("999");
        } catch (ContaNaoEncontradaException e) {
            System.out.println(
                    "Erro na busca: " + e.getMessage());
        }

        System.out.println(
                "Aplicação continua funcionando.");

        ContaBancaria contaCorrente = new ContaCorrente(
                "Arthur",
                "001");

        ContaBancaria contaPoupanca = new ContaPoupanca(
                "Maria",
                "002");

        contaService.adicionarConta(contaCorrente);
        contaService.adicionarConta(contaPoupanca);

        contaCorrente.depositar(200);
        contaPoupanca.depositar(200);

        contaService.sacar("001", 100);
        contaService.sacar("002", 100);

        System.out.println(
                "Saldo da conta 1: "
                        + contaCorrente.getSaldo());

        System.out.println(
                "Saldo da conta 2: "
                        + contaPoupanca.getSaldo());

        contaService.aplicarRendimento(
                "002",
                2);

        System.out.println(
                "Saldo da conta 2 após rendimento: "
                        + contaPoupanca.getSaldo());

        try {
            contaService.aplicarRendimento(
                    "001",
                    2);
        } catch (ContaNaoRendivelException e) {
            System.out.println(
                    "Erro ao aplicar rendimento na conta 1: "
                            + e.getMessage());
        }

        System.out.println(
                "Programa finalizado.");
    }
}
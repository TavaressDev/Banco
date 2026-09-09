package br.com.arthur.banco;

public class TesteConexao {

        public static void main(String[] args) {

                ContaRepository repository = new ContaRepositoryPostgres();

                ContaBancaria conta = repository
                                .buscarPorNumero("004")
                                .orElseThrow();

                System.out.println(
                                "Saldo antes: "
                                                + conta.getSaldo());

                conta.depositar(50);

                repository.salvar(conta);

                ContaBancaria contaDepois = repository
                                .buscarPorNumero("004")
                                .orElseThrow();

                System.out.println(
                                "Saldo depois: "
                                                + contaDepois.getSaldo());
        }
}
package br.com.arthur.banco;

public class TesteConexao {

        public static void main(String[] args) {

                ContaRepository repository = new ContaRepositoryPostgres();

                for (ContaBancaria conta : repository.listarTodas()) {

                        System.out.println(
                                        conta.getNumero()
                                                        + " | "
                                                        + conta.getTitular()
                                                        + " | "
                                                        + conta.getSaldo()
                                                        + " | transações: "
                                                        + conta
                                                                        .getTransacaosHistorico()
                                                                        .size());
                }
        }
}
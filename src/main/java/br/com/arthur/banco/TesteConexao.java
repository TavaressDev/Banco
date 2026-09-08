package br.com.arthur.banco;

public class TesteConexao {

        public static void main(String[] args) {

                ContaRepository repository = new ContaRepositoryPostgres();

                ContaService service = new ContaService(repository);

                ContaBancaria conta = service.buscarContaPorNumero("002");

                System.out.println(
                                conta.getNumero()
                                                + " | "
                                                + conta.getTitular()
                                                + " | "
                                                + conta.getSaldo()
                                                + " | "
                                                + conta.getClass().getSimpleName());
        }
}
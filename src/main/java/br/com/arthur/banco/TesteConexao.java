package br.com.arthur.banco;

public class TesteConexao {

        public static void main(String[] args) {

                DatabaseConfig config = DatabaseConfig.fromEnvironment();

                ContaRepository repository = new ContaRepositoryPostgres(config);

                ContaBancaria conta = repository
                                .buscarPorNumero("004")
                                .orElseThrow();

                System.out.println(
                                conta.getNumero()
                                                + " | "
                                                + conta.getTitular()
                                                + " | "
                                                + conta.getSaldo());
        }
}
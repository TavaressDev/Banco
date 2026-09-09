package br.com.arthur.banco;

import com.zaxxer.hikari.HikariDataSource;

public class TesteConexao {

        public static void main(String[] args) {

                DatabaseConfig config = DatabaseConfig.fromEnvironment();

                try (
                                HikariDataSource dataSource = DatabaseDataSource.criar(config)) {

                        DatabaseMigration.migrate(
                                        dataSource);

                        ContaRepository repository = new ContaRepositoryPostgres(
                                        dataSource);

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
}
package br.com.arthur.banco;

import java.util.List;

public class TesteConexao {

        public static void main(String[] args) {

                ContaRepository repository = new ContaRepositoryPostgres();

                ContaBancaria conta = repository
                                .buscarPorNumero("001")
                                .orElseThrow();

                System.out.println(
                                "Saldo no banco antes: " + conta.getSaldo());

                // Altera o objeto em memória
                conta.depositar(50);

                ContaBancaria contaInvalida = new ContaBancaria(
                                "Conta Teste",
                                "999") {
                        @Override
                        protected double obterTarifaSaque() {
                                return 0;
                        }
                };

                try {

                        repository.salvarTodas(
                                        List.of(
                                                        conta,
                                                        contaInvalida));

                } catch (RuntimeException e) {

                        System.out.println(
                                        "Falha proposital: " + e.getMessage());
                }

                ContaBancaria contaDepois = repository
                                .buscarPorNumero("001")
                                .orElseThrow();

                System.out.println(
                                "Saldo do objeto em memória: "
                                                + conta.getSaldo());

                System.out.println(
                                "Saldo no banco depois: "
                                                + contaDepois.getSaldo());
        }
}
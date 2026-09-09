package br.com.arthur.banco;

public class TesteConexao {

        public static void main(String[] args) {

                ContaRepository repository = new ContaRepositoryPostgres();

                ContaBancaria conta = repository
                                .buscarPorNumero("004")
                                .orElseThrow();

                System.out.println(
                                "Saldo: " + conta.getSaldo());

                System.out.println(
                                "Quantidade de transações: "
                                                + conta
                                                                .getTransacaosHistorico()
                                                                .size());

                for (Transacao transacao : conta.getTransacaosHistorico()) {

                        System.out.println(
                                        transacao.getId()
                                                        + " | "
                                                        + transacao.getTipo()
                                                        + " | "
                                                        + transacao.getValor()
                                                        + " | "
                                                        + transacao.getDataHoraFormatada());
                }
        }
}
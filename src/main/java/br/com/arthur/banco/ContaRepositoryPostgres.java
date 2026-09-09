package br.com.arthur.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContaRepositoryPostgres implements ContaRepository {

    private final String url = "jdbc:postgresql://localhost:5433/sistema_bancario";

    private final String usuario = "banco_user";

    private final String senha = "banco_password";

    @Override
    public Optional<ContaBancaria> buscarPorNumero(String numero) {

        String sql = """
                SELECT
                    numero,
                    titular,
                    tipo,
                    saldo
                FROM contas
                WHERE numero = ?
                """;

        try (
                Connection connection = DriverManager.getConnection(url, usuario, senha);

                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numero);

            try (
                    ResultSet resultado = statement.executeQuery()) {

                if (!resultado.next()) {
                    return Optional.empty();
                }

                ContaBancaria conta = mapearConta(resultado);

                return Optional.of(conta);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar conta no banco",
                    e);
        }
    }

    @Override
    public void salvar(ContaBancaria conta) {

        try (
                Connection connection = DriverManager.getConnection(
                        url,
                        usuario,
                        senha)) {

            connection.setAutoCommit(false);

            try {

                salvar(
                        connection,
                        conta);

                connection.commit();

            } catch (Exception e) {

                connection.rollback();

                throw e;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar conta no banco",
                    e);
        }
    }

    private void salvar(
            Connection connection,
            ContaBancaria conta) throws SQLException {

        long contaId = salvarConta(
                connection,
                conta);

        salvarTransacoes(
                connection,
                contaId,
                conta);
    }

    @Override
    public List<ContaBancaria> listarTodas() {

        String sql = """
                SELECT
                    numero,
                    titular,
                    tipo,
                    saldo
                FROM contas
                ORDER BY id
                """;

        List<ContaBancaria> contas = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(
                        url,
                        usuario,
                        senha);

                PreparedStatement statement = connection.prepareStatement(sql);

                ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                ContaBancaria conta = mapearConta(resultado);

                contas.add(conta);
            }

            return contas;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar contas do banco",
                    e);
        }
    }

    private ContaBancaria mapearConta(
            ResultSet resultado) throws SQLException {

        String numero = resultado.getString("numero");

        String titular = resultado.getString("titular");

        String tipo = resultado.getString("tipo");

        double saldo = resultado.getDouble("saldo");

        return switch (tipo) {

            case "CORRENTE" ->
                ContaCorrente.reidratar(
                        titular,
                        numero,
                        saldo);

            case "POUPANCA" ->
                ContaPoupanca.reidratar(
                        titular,
                        numero,
                        saldo);

            default ->
                throw new IllegalStateException(
                        "Tipo de conta desconhecido: " + tipo);
        };
    }

    private long salvarConta(
            Connection connection,
            ContaBancaria conta) throws SQLException {

        String sql = """
                INSERT INTO contas (
                    numero,
                    titular,
                    tipo,
                    saldo
                )
                VALUES (?, ?, ?, ?)

                ON CONFLICT (numero)
                DO UPDATE SET
                    titular = EXCLUDED.titular,
                    tipo = EXCLUDED.tipo,
                    saldo = EXCLUDED.saldo

                RETURNING id
                """;

        String tipo;

        if (conta instanceof ContaCorrente) {
            tipo = "CORRENTE";

        } else if (conta instanceof ContaPoupanca) {
            tipo = "POUPANCA";

        } else {
            throw new IllegalArgumentException(
                    "Tipo de conta não suportado: "
                            + conta.getClass().getSimpleName());
        }

        try (
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    conta.getNumero());

            statement.setString(
                    2,
                    conta.getTitular());

            statement.setString(
                    3,
                    tipo);

            statement.setDouble(
                    4,
                    conta.getSaldo());

            try (
                    ResultSet resultado = statement.executeQuery()) {

                if (!resultado.next()) {
                    throw new SQLException(
                            "Não foi possível obter o ID da conta.");
                }

                return resultado.getLong("id");
            }
        }
    }

    private void salvarTransacoes(
            Connection connection,
            long contaId,
            ContaBancaria conta) throws SQLException {

        String sql = """
                INSERT INTO transacoes (
                    identificador,
                    conta_id,
                    tipo,
                    valor,
                    data_hora
                )
                VALUES (?, ?, ?, ?, ?)

                ON CONFLICT (identificador)
                DO NOTHING
                """;

        try (
                PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Transacao transacao : conta.getTransacaosHistorico()) {

                statement.setObject(
                        1,
                        transacao.getId());

                statement.setLong(
                        2,
                        contaId);

                statement.setString(
                        3,
                        transacao.getTipo().name());

                statement.setDouble(
                        4,
                        transacao.getValor());

                statement.setObject(
                        5,
                        transacao.getDataHora());

                statement.executeUpdate();
            }
        }
    }

    @Override
    public void salvarTodas(
            List<ContaBancaria> contas) {

        try (
                Connection connection = DriverManager.getConnection(
                        url,
                        usuario,
                        senha)) {

            connection.setAutoCommit(false);

            try {

                for (ContaBancaria conta : contas) {
                    salvar(
                            connection,
                            conta);
                }

                connection.commit();

            } catch (Exception e) {

                connection.rollback();

                throw e;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar contas no banco",
                    e);
        }
    }
}
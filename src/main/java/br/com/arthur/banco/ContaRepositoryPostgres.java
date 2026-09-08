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
                Connection connection = DriverManager.getConnection(
                        url,
                        usuario,
                        senha);

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

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar conta no banco",
                    e);
        }
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
}
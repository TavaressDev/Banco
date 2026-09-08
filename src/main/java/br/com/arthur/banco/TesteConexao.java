package br.com.arthur.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5433/sistema_bancario";
        String usuario = "banco_user";
        String senha = "banco_password";

        String sql = """
                SELECT
                    id,
                    numero,
                    titular,
                    tipo,
                    saldo
                FROM contas
                WHERE numero = ?
                """;

        try (
                Connection connection =
                        DriverManager.getConnection(url, usuario, senha);

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, "002");

            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                if (resultado.next()) {

                    long id =
                            resultado.getLong("id");

                    String numero =
                            resultado.getString("numero");

                    String titular =
                            resultado.getString("titular");

                    String tipo =
                            resultado.getString("tipo");

                    double saldo =
                            resultado.getDouble("saldo");

                    System.out.println(
                            id + " | "
                                    + numero + " | "
                                    + titular + " | "
                                    + tipo + " | "
                                    + saldo
                    );

                } else {
                    System.out.println("Conta não encontrada");
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro no banco: " + e.getMessage()
            );
        }
    }
}
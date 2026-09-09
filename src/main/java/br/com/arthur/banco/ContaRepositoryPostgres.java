package br.com.arthur.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ContaRepositoryPostgres implements ContaRepository {

    private final String url;
    private final String usuario;
    private final String senha;

    public ContaRepositoryPostgres(
            DatabaseConfig config) {
        if (config == null) {
            throw new IllegalArgumentException(
                    "Configuração do banco não pode ser nula.");
        }

        this.url = config.getUrl();
        this.usuario = config.getUsuario();
        this.senha = config.getSenha();
    }

    @Override
    public Optional<ContaBancaria> buscarPorNumero(
            String numero) {

        String sql = """
                SELECT
                    id,
                    numero,
                    titular,
                    tipo AS conta_tipo,
                    saldo
                FROM contas
                WHERE numero = ?
                """;

        try (
                Connection connection = DriverManager.getConnection(
                        url,
                        usuario,
                        senha);

                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numero);

            ContaBancaria conta;
            long contaId;

            try (
                    ResultSet resultado = statement.executeQuery()) {

                if (!resultado.next()) {
                    return Optional.empty();
                }

                contaId = resultado.getLong("id");

                conta = mapearConta(resultado);
            }

            List<Transacao> transacoes = buscarTransacoes(
                    connection,
                    contaId);

            conta.reidratarHistorico(
                    transacoes);

            return Optional.of(conta);

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar conta no banco",
                    e);
        }
    }

    @Override
    public void salvar(
            ContaBancaria conta) {

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
            ContaBancaria conta)
            throws SQLException {

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
                    c.id AS conta_id,
                    c.numero,
                    c.titular,
                    c.tipo AS conta_tipo,
                    c.saldo,

                    t.identificador,
                    t.tipo AS transacao_tipo,
                    t.valor,
                    t.data_hora

                FROM contas c

                LEFT JOIN transacoes t
                    ON t.conta_id = c.id

                ORDER BY
                    c.id,
                    t.data_hora,
                    t.id
                """;

        Map<Long, ContaBancaria> contas = new LinkedHashMap<>();

        Map<Long, List<Transacao>> historicos = new LinkedHashMap<>();

        try (
                Connection connection = DriverManager.getConnection(
                        url,
                        usuario,
                        senha);

                PreparedStatement statement = connection.prepareStatement(sql);

                ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                long contaId = resultado.getLong("conta_id");

                if (!contas.containsKey(contaId)) {

                    ContaBancaria conta = mapearConta(resultado);

                    contas.put(
                            contaId,
                            conta);

                    historicos.put(
                            contaId,
                            new ArrayList<>());
                }

                Object identificador = resultado.getObject(
                        "identificador");

                if (identificador != null) {

                    Transacao transacao = mapearTransacao(resultado);

                    historicos
                            .get(contaId)
                            .add(transacao);
                }
            }

            for (Map.Entry<Long, ContaBancaria> entry : contas.entrySet()) {

                long contaId = entry.getKey();

                ContaBancaria conta = entry.getValue();

                conta.reidratarHistorico(
                        historicos.get(contaId));
            }

            return new ArrayList<>(
                    contas.values());

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar contas do banco",
                    e);
        }
    }

    private ContaBancaria mapearConta(
            ResultSet resultado)
            throws SQLException {

        String numero = resultado.getString("numero");

        String titular = resultado.getString("titular");

        String tipo = resultado.getString("conta_tipo");

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
                        "Tipo de conta desconhecido: "
                                + tipo);
        };
    }

    private long salvarConta(
            Connection connection,
            ContaBancaria conta)
            throws SQLException {

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
                            + conta
                                    .getClass()
                                    .getSimpleName());
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
            ContaBancaria conta)
            throws SQLException {

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
                        transacao
                                .getTipo()
                                .name());

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

    private List<Transacao> buscarTransacoes(
            Connection connection,
            long contaId)
            throws SQLException {

        String sql = """
                SELECT
                    identificador,
                    tipo,
                    valor,
                    data_hora
                FROM transacoes
                WHERE conta_id = ?
                ORDER BY data_hora, id
                """;

        List<Transacao> transacoes = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    contaId);

            try (
                    ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {

                    UUID identificador = resultado.getObject(
                            "identificador",
                            UUID.class);

                    TipoTransacao tipo = TipoTransacao.valueOf(
                            resultado.getString(
                                    "tipo"));

                    double valor = resultado.getDouble(
                            "valor");

                    Transacao transacao = Transacao.reidratar(
                            identificador,
                            tipo,
                            valor,
                            resultado
                                    .getTimestamp(
                                            "data_hora")
                                    .toLocalDateTime());

                    transacoes.add(
                            transacao);
                }
            }
        }

        return transacoes;
    }

    private Transacao mapearTransacao(
            ResultSet resultado)
            throws SQLException {

        UUID identificador = resultado.getObject(
                "identificador",
                UUID.class);

        TipoTransacao tipo = TipoTransacao.valueOf(
                resultado.getString(
                        "transacao_tipo"));

        double valor = resultado.getDouble(
                "valor");

        return Transacao.reidratar(
                identificador,
                tipo,
                valor,
                resultado
                        .getTimestamp(
                                "data_hora")
                        .toLocalDateTime());
    }
}
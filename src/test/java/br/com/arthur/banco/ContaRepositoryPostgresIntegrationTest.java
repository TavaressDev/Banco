package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.zaxxer.hikari.HikariDataSource;

@Testcontainers
class ContaRepositoryPostgresIntegrationTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17")
            .withDatabaseName("sistema_bancario_test")
            .withUsername("test_user")
            .withPassword("test_password");

    private HikariDataSource dataSource;

    private ContaRepository repository;

    @BeforeEach
    void setUp() {

        DatabaseConfig config = new DatabaseConfig(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());

        dataSource = DatabaseDataSource.criar(config);

        DatabaseMigration.migrate(
                dataSource);

        repository = new ContaRepositoryPostgres(
                dataSource);
    }

    @AfterEach
    void tearDown() {

        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    void deveSalvarEBuscarContaNoPostgres() {

        ContaBancaria conta = new ContaCorrente(
                "Arthur",
                "999");

        conta.depositar(500);

        repository.salvar(conta);

        var resultado = repository.buscarPorNumero(
                "999");

        assertTrue(
                resultado.isPresent());

        ContaBancaria contaEncontrada = resultado.orElseThrow();

        assertEquals(
                "999",
                contaEncontrada.getNumero());

        assertEquals(
                "Arthur",
                contaEncontrada.getTitular());

        assertEquals(
                500.0,
                contaEncontrada.getSaldo());
    }

    @Test
    void devePersistirEReidratarHistoricoDeTransacoes() {

        ContaBancaria conta = new ContaCorrente(
                "Maria",
                "998");

        conta.depositar(100);
        conta.depositar(50);

        repository.salvar(conta);

        ContaBancaria encontrada = repository
                .buscarPorNumero("998")
                .orElseThrow();

        assertEquals(
                150.0,
                encontrada.getSaldo());

        assertEquals(
                2,
                encontrada
                        .getTransacaosHistorico()
                        .size());

        assertEquals(
                TipoTransacao.DEPOSITO,
                encontrada
                        .getTransacaosHistorico()
                        .get(0)
                        .getTipo());

        assertEquals(
                100.0,
                encontrada
                        .getTransacaosHistorico()
                        .get(0)
                        .getValor());

        assertEquals(
                50.0,
                encontrada
                        .getTransacaosHistorico()
                        .get(1)
                        .getValor());
    }

    @Test
    void naoDeveDuplicarTransacoesAoSalvarContaNovamente() {

        ContaBancaria conta = new ContaCorrente(
                "Carlos",
                "997");

        conta.depositar(100);

        repository.salvar(conta);

        conta.depositar(50);

        repository.salvar(conta);

        ContaBancaria encontrada = repository
                .buscarPorNumero("997")
                .orElseThrow();

        assertEquals(
                150.0,
                encontrada.getSaldo());

        assertEquals(
                2,
                encontrada
                        .getTransacaosHistorico()
                        .size());
    }

    @Test
    void deveFazerRollbackQuandoUmaDasContasFalhar() {

        ContaBancaria contaValida = new ContaCorrente(
                "Arthur",
                "996");

        contaValida.depositar(100);

        ContaBancaria contaInvalidaParaOBanco = new ContaCorrente(
                "X".repeat(101),
                "995");

        contaInvalidaParaOBanco.depositar(200);

        assertThrows(
                PersistenciaException.class,
                () -> repository.salvarTodas(
                        List.of(
                                contaValida,
                                contaInvalidaParaOBanco)));

        assertFalse(
                repository
                        .buscarPorNumero("996")
                        .isPresent());

        assertFalse(
                repository
                        .buscarPorNumero("995")
                        .isPresent());
    }
}
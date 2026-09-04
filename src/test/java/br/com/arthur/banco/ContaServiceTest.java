package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaServiceTest {

    private ContaRepository repository;
    private ContaService service;

    @BeforeEach
    void setUp() {
        repository = new ContaRepositoryEmMemoria();
        service = new ContaService(repository);
    }

    @Test
    void deveAdicionarEBuscarConta() {
        ContaCorrente conta = new ContaCorrente("Arthur", "001");

        service.adicionarConta(conta);

        ContaBancaria contaEncontrada = service.buscarContaPorNumero("001");

        assertSame(conta, contaEncontrada);
    }

    @Test
    void naoDevePermitirContaComNumeroDuplicado() {
        ContaCorrente primeiraConta = new ContaCorrente("Arthur", "001");

        ContaPoupanca segundaConta = new ContaPoupanca("Maria", "001");

        service.adicionarConta(primeiraConta);

        ContaJaExisteException exception = assertThrows(
                ContaJaExisteException.class,
                () -> service.adicionarConta(segundaConta));

        assertEquals(
                "Já existe uma conta com o número: 001",
                exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoExistir() {
        ContaNaoEncontradaException exception = assertThrows(
                ContaNaoEncontradaException.class,
                () -> service.buscarContaPorNumero("999"));

        assertEquals(
                "Conta não encontrada: 999",
                exception.getMessage());
    }

    @Test
    void deveTransferirEntreDuasContas() {
        ContaPoupanca origem = new ContaPoupanca("Arthur", "001");

        ContaPoupanca destino = new ContaPoupanca("Maria", "002");

        origem.depositar(1000.0);

        service.adicionarConta(origem);
        service.adicionarConta(destino);

        service.transferir(
                "001",
                "002",
                250.0);

        assertEquals(
                750.0,
                origem.getSaldo(),
                0.001);

        assertEquals(
                250.0,
                destino.getSaldo(),
                0.001);
    }

    @Test
    void deveSacarDeUmaConta() {
        ContaPoupanca conta = new ContaPoupanca("Arthur", "001");

        conta.depositar(500.0);

        service.adicionarConta(conta);

        service.sacar("001", 100.0);

        assertEquals(
                400.0,
                conta.getSaldo(),
                0.001);
    }

    @Test
    void deveAplicarRendimentoEmContaPoupanca() {
        ContaPoupanca conta = new ContaPoupanca("Arthur", "001");

        conta.depositar(1000.0);

        service.adicionarConta(conta);

        service.aplicarRendimento(
                "001",
                10.0);

        assertEquals(
                1100.0,
                conta.getSaldo(),
                0.001);
    }

    @Test
    void deveLancarExcecaoAoAplicarRendimentoEmContaNaoRendivel() {
        ContaCorrente conta = new ContaCorrente("Arthur", "001");

        conta.depositar(1000.0);

        service.adicionarConta(conta);

        assertThrows(
                ContaNaoRendivelException.class,
                () -> service.aplicarRendimento(
                        "001",
                        10.0));
    }

    @Test
    void deveListarResumoDasContas() {
        ContaPoupanca conta = new ContaPoupanca("Arthur", "001");

        conta.depositar(100.0);

        service.adicionarConta(conta);

        var resumos = service.listarResumosDasContas();

        assertEquals(1, resumos.size());

        ContaResumo resumo = resumos.get(0);

        assertAll(
                () -> assertEquals("001", resumo.numero()),
                () -> assertEquals("Arthur", resumo.titular()),
                () -> assertEquals(
                        100.0,
                        resumo.saldo(),
                        0.001));
    }

    @Test
    void deveListarTodasAsContas() {
        service.adicionarConta(
                new ContaCorrente("Arthur", "001"));

        service.adicionarConta(
                new ContaPoupanca("Maria", "002"));

        List<ContaBancaria> contas = service.listarContas();

        assertEquals(2, contas.size());
    }

    @Test
    void deveBuscarContasComSaldoMaiorQue() {
        ContaPoupanca conta1 = new ContaPoupanca("Arthur", "001");
        conta1.depositar(100.0);

        ContaCorrente conta2 = new ContaCorrente("Maria", "002");
        conta2.depositar(500.0);

        ContaPoupanca conta3 = new ContaPoupanca("Pedro", "003");
        conta3.depositar(200.0);

        service.adicionarConta(conta1);
        service.adicionarConta(conta2);
        service.adicionarConta(conta3);

        var contas = service.buscarContasComSaldoMaiorQue(150.0);

        assertEquals(2, contas.size());

        assertTrue(contas.contains(conta2));
        assertTrue(contas.contains(conta3));
        assertFalse(contas.contains(conta1));
    }

    @Test
    void deveListarNumerosDasContas() {
        service.adicionarConta(
                new ContaCorrente("Arthur", "001"));

        service.adicionarConta(
                new ContaPoupanca("Maria", "002"));

        List<String> numeros = service.listarNumerosDasContas();

        assertEquals(2, numeros.size());
        assertTrue(numeros.contains("001"));
        assertTrue(numeros.contains("002"));
    }

    @Test
    void deveListarNumerosDasContasComSaldoMaiorQue() {
        ContaPoupanca conta1 = new ContaPoupanca("Arthur", "001");
        conta1.depositar(100.0);

        ContaCorrente conta2 = new ContaCorrente("Maria", "002");
        conta2.depositar(500.0);

        service.adicionarConta(conta1);
        service.adicionarConta(conta2);

        var numeros = service.listarNumerosDasContasComSaldoMaiorQue(150.0);

        assertEquals(1, numeros.size());
        assertEquals("002", numeros.get(0));
    }

    @Test
    void deveCalcularSaldoTotal() {
        service.adicionarConta(
                new ContaPoupanca("Arthur", "001"));
        service.adicionarConta(
                new ContaCorrente("Maria", "002"));

        ContaBancaria conta1 = service.buscarContaPorNumero("001");
        conta1.depositar(1000.0);

        ContaBancaria conta2 = service.buscarContaPorNumero("002");
        conta2.depositar(500.0);

        assertEquals(1500.0, service.calcularSaldoTotal(), 0.001);
    }

    @Test
    void deveListarContasOrdenadasPorSaldo() {
        ContaCorrente conta1 = new ContaCorrente("Arthur", "001");
        conta1.depositar(100.0);

        ContaPoupanca conta2 = new ContaPoupanca("Maria", "002");
        conta2.depositar(500.0);

        ContaPoupanca conta3 = new ContaPoupanca("Pedro", "003");
        conta3.depositar(200.0);

        service.adicionarConta(conta1);
        service.adicionarConta(conta2);
        service.adicionarConta(conta3);

        List<ContaBancaria> contas = service.listarContasOrdenadasPorSaldo();

        assertEquals(3, contas.size());
        assertSame(conta1, contas.get(0));
        assertSame(conta3, contas.get(1));
        assertSame(conta2, contas.get(2));
    }

    @Test
    void deveBuscarPrimeiraContaComSaldoMaiorQue() {
        ContaPoupanca conta1 = new ContaPoupanca("Arthur", "001");
        conta1.depositar(100.0);

        ContaCorrente conta2 = new ContaCorrente("Maria", "002");
        conta2.depositar(500.0);

        service.adicionarConta(conta1);
        service.adicionarConta(conta2);

        ContaBancaria primeira = service.buscarPrimeiraContaComSaldoMaiorQue(150.0);

        assertSame(conta2, primeira);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarContaComSaldoMaiorQue() {
        ContaPoupanca conta = new ContaPoupanca("Arthur", "001");
        conta.depositar(100.0);

        service.adicionarConta(conta);

        assertThrows(
                ContaNaoEncontradaException.class,
                () -> service.buscarPrimeiraContaComSaldoMaiorQue(1000.0));
    }

    @Test
    void deveVerificarSeExisteContaComSaldoMaiorQue() {
        ContaPoupanca conta = new ContaPoupanca("Arthur", "001");
        conta.depositar(100.0);

        service.adicionarConta(conta);

        assertTrue(service.existeContaComSaldoMaiorQue(50.0));
        assertFalse(service.existeContaComSaldoMaiorQue(200.0));
    }

    @Test
    void deveContarContasComSaldoMaiorQue() {
        ContaPoupanca conta1 = new ContaPoupanca("Arthur", "001");
        conta1.depositar(100.0);

        ContaCorrente conta2 = new ContaCorrente("Maria", "002");
        conta2.depositar(500.0);

        ContaPoupanca conta3 = new ContaPoupanca("Pedro", "003");
        conta3.depositar(200.0);

        service.adicionarConta(conta1);
        service.adicionarConta(conta2);
        service.adicionarConta(conta3);

        long quantidade = service.contarContasComSaldoMaiorQue(150.0);

        assertEquals(2, quantidade);
    }

    @Test
    void deveBuscarTitularDaConta() {
        ContaPoupanca conta = new ContaPoupanca("Arthur", "001");

        service.adicionarConta(conta);

        Optional<String> titular = service.buscarTitularDaConta("001");

        assertTrue(titular.isPresent());
        assertEquals("Arthur", titular.get());
    }

    @Test
    void deveRetornarOptionalVazioAoBuscarTitularDeContaInexistente() {
        Optional<String> titular = service.buscarTitularDaConta("999");

        assertFalse(titular.isPresent());
    }
}
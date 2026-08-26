package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BancoTest {

    private Banco banco;

    @BeforeEach
    void setUp() {
        banco = new Banco();
    }

    @Test
    void deveLancarExcecaoAoBuscarContaInexistente() {
        // Arrange
        Banco banco = new Banco();

        // Act + Assert
        ContaNaoEncontradaException exception = assertThrows(
            ContaNaoEncontradaException.class,
            () -> banco.buscarContaPorNumero("999")
        );
        
        assertEquals(
            "Conta com número 999 não encontrada.",
            exception.getMessage()
        );
    }

    @Test
    void deveAdicionarEBuscarConta(){
        ContaCorrente conta = new ContaCorrente("Carlos", "003");
        banco.adicionarConta(conta);
        banco.buscarContaPorNumero("003");
        assertSame(conta, banco.buscarContaPorNumero("003"));
    }

    @Test
    void deveLancarExcecaoAoAdicionarContaExistente() {
        ContaCorrente conta1 = new ContaCorrente("Carlos", "003");
        ContaCorrente conta2 = new ContaCorrente("Ana", "003");
        banco.adicionarConta(conta1);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> banco.adicionarConta(conta2)
        );
        
        assertEquals(
            "Já existe uma conta com o mesmo número.",
            exception.getMessage()
        );
    }

    @Test
    void deveTransferirEntreContas() {
        ContaPoupanca contaOrigem = new ContaPoupanca("Carlos", "003");
        ContaPoupanca contaDestino = new ContaPoupanca("Ana", "004");

        banco.adicionarConta(contaOrigem);
        banco.adicionarConta(contaDestino);

        contaOrigem.depositar(100.0);

        banco.transferir("003", "004", 50.0);

        assertEquals(50.0, contaOrigem.getSaldo(), 0.001);
        assertEquals(50.0, contaDestino.getSaldo(), 0.001);
    }

    @Test
    void naoDeveTransferirQuandoTransferenciaFalhar(){
        ContaPoupanca contaOrigem = new ContaPoupanca("Carlos", "003");
        ContaPoupanca contaDestino = new ContaPoupanca("Ana", "004");

        banco.adicionarConta(contaOrigem);
        banco.adicionarConta(contaDestino);

        contaOrigem.depositar(100.0);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> banco.transferir("003", "004", 150.0)
        );
        
        assertEquals(
            "Saldo insuficiente para realizar o saque.",
            exception.getMessage()
        );

        // Verifica que os saldos permanecem inalterados
        assertEquals(100.0, contaOrigem.getSaldo(), 0.001);
        assertEquals(0.0, contaDestino.getSaldo(), 0.001);
    }

    @Test
    void deveAplicarRendimentoNaContaPoupanca() {
        ContaPoupanca conta = new ContaPoupanca("Carlos", "005");
        banco.adicionarConta(conta);

        conta.depositar(200.0);
        banco.aplicarRendimento("005", 2.0); // 2% de rendimento

        assertEquals(204.0, conta.getSaldo(), 0.001);
    }

    @Test
    void deveRegistrarTransferenciaNoHistoricoDasContas() {
        ContaPoupanca contaOrigem =
            new ContaPoupanca("Carlos", "010");

        ContaPoupanca contaDestino =
            new ContaPoupanca("Ana", "011");

        banco.adicionarConta(contaOrigem);
        banco.adicionarConta(contaDestino);

        contaOrigem.depositar(100.0);

        banco.transferir("010", "011", 40.0);

        List<Transacao> historicoOrigem =
            contaOrigem.getTransacaosHistorico();

        List<Transacao> historicoDestino =
            contaDestino.getTransacaosHistorico();

        assertAll(
            () -> assertEquals(
                TipoTransacao.TRANSFERENCIA,
                historicoOrigem.get(1).getTipo()
            ),
            () -> assertEquals(
                40.0,
                historicoOrigem.get(1).getValor(),
                0.001
            ),
            () -> assertEquals(
                TipoTransacao.TRANSFERENCIA,
                historicoDestino.get(0).getTipo()
            ),
            () -> assertEquals(
                40.0,
                historicoDestino.get(0).getValor(),
                0.001
            ),
            () -> assertEquals(
                60.0,
                contaOrigem.getSaldo(),
                0.001
            ),
            () -> assertEquals(
                40.0,
                contaDestino.getSaldo(),
                0.001
            )
        );
    }

    @Test
    void deveListarContasCadastradas() {
        ContaCorrente contaCorrente =
            new ContaCorrente("Carlos", "001");

        ContaPoupanca contaPoupanca =
            new ContaPoupanca("Ana", "002");

        banco.adicionarConta(contaCorrente);
        banco.adicionarConta(contaPoupanca);

        List<ContaBancaria> contas = banco.listarContas();

        assertAll(
            () -> assertEquals(2, contas.size()),
            () -> assertTrue(contas.contains(contaCorrente)),
            () -> assertTrue(contas.contains(contaPoupanca))
        );
    }

    @Test
    void deveBuscarContasComSaldoMaiorQueValorInformado() {
        ContaPoupanca conta1 =
            new ContaPoupanca("Carlos", "001");

        ContaPoupanca conta2 =
            new ContaPoupanca("Ana", "002");

        ContaPoupanca conta3 =
            new ContaPoupanca("Maria", "003");

        conta1.depositar(50.0);
        conta2.depositar(200.0);
        conta3.depositar(300.0);

        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);
        banco.adicionarConta(conta3);

        List<ContaBancaria> resultado =
            banco.buscarContasComSaldoMaiorQue(100.0);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(conta2));
        assertTrue(resultado.contains(conta3));
    }

    @Test
    void deveListarNumerosDasContas() {
        ContaCorrente conta1 =
            new ContaCorrente("Carlos", "001");

        ContaPoupanca conta2 =
            new ContaPoupanca("Ana", "002");

        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);

        List<String> numeros =
            banco.listarNumerosDasContas();

        assertEquals(2, numeros.size());
        assertTrue(numeros.contains("001"));
        assertTrue(numeros.contains("002"));
    }

    @Test
    void deveListarNumerosDasContasComSaldoMaiorQueValor() {
        ContaPoupanca conta1 =
            new ContaPoupanca("Carlos", "001");

        ContaPoupanca conta2 =
            new ContaPoupanca("Ana", "002");

        ContaPoupanca conta3 =
            new ContaPoupanca("Maria", "003");

        conta1.depositar(50.0);
        conta2.depositar(200.0);
        conta3.depositar(300.0);

        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);
        banco.adicionarConta(conta3);

        List<String> numeros =
            banco.listarNumerosDasContasComSaldoMaiorQue(100.0);

        assertEquals(2, numeros.size());
        assertTrue(numeros.contains("002"));
        assertTrue(numeros.contains("003"));
    }

    @Test
    void deveCalcularSaldoTotalDasContas() {
        ContaPoupanca conta1 =
            new ContaPoupanca("Carlos", "001");

        ContaPoupanca conta2 =
            new ContaPoupanca("Ana", "002");

        ContaPoupanca conta3 =
            new ContaPoupanca("Maria", "003");

        conta1.depositar(100.0);
        conta2.depositar(200.0);
        conta3.depositar(300.0);

        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);
        banco.adicionarConta(conta3);

        double total = banco.calcularSaldoTotal();

        assertEquals(600.0, total, 0.001);
    }

    @Test
    void deveListarContasOrdenadasPorSaldo() {
        ContaPoupanca conta1 =
            new ContaPoupanca("Carlos", "001");

        ContaPoupanca conta2 =
            new ContaPoupanca("Ana", "002");

        ContaPoupanca conta3 =
            new ContaPoupanca("Maria", "003");

        conta1.depositar(300.0);
        conta2.depositar(50.0);
        conta3.depositar(200.0);

        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);
        banco.adicionarConta(conta3);

        List<ContaBancaria> resultado =
            banco.listarContasOrdenadasPorSaldo();

        assertAll(
            () -> assertSame(conta2, resultado.get(0)),
            () -> assertSame(conta3, resultado.get(1)),
            () -> assertSame(conta1, resultado.get(2))
        );
    }

    @Test
    void deveVerificarSeExisteContaComSaldoMaiorQueValor() {
        ContaPoupanca conta = new ContaPoupanca("Carlos", "001");
        conta.depositar(200.0);
        banco.adicionarConta(conta);

        assertTrue(
            banco.existeContaComSaldoMaiorQue(100.0)
        );
    }

    @Test
    void deveContarContasComSaldoMaiorQueValor() {
        ContaPoupanca conta1 = new ContaPoupanca("Carlos", "001");
        ContaPoupanca conta2 = new ContaPoupanca("Ana", "002");
        ContaPoupanca conta3 = new ContaPoupanca("Maria", "003");

        conta1.depositar(50.0);
        conta2.depositar(200.0);
        conta3.depositar(300.0);

        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);
        banco.adicionarConta(conta3);

        assertEquals(
            2,
            banco.contarContasComSaldoMaiorQue(100.0)
        );
    }
}

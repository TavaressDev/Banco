import java.util.List;

@Test
void deveCalcularSaldoDeContasCorrentes() {
    ContaCorrente conta1 =
        new ContaCorrente("Carlos", "001");

    ContaCorrente conta2 =
        new ContaCorrente("Maria", "002");

    conta1.depositar(100.0);
    conta2.depositar(200.0);

    List<ContaCorrente> contas =
        List.of(conta1, conta2);

    RelatorioContas relatorio =
        new RelatorioContas();

    assertEquals(
        300.0,
        relatorio.calcularSaldoTotal(contas),
        0.001
    );
}
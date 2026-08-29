package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HistoricoArquivoServiceTest {

    @TempDir
    Path pastaTemporaria;

    @Test
    void deveExportarHistoricoDaConta() throws IOException {
        ContaPoupanca conta =
            new ContaPoupanca("Maria", "001");

        conta.depositar(100.0);
        conta.sacar(40.0);

        Path arquivo =
            pastaTemporaria.resolve("historico.txt");

        HistoricoArquivoService service =
            new HistoricoArquivoService();

        service.exportar(conta, arquivo);

        assertTrue(Files.exists(arquivo));

        List<String> linhas =
            Files.readAllLines(arquivo);

        assertEquals(2, linhas.size());

        assertTrue(
            linhas.get(0).startsWith("DEPOSITO;100.0;")
        );

        assertTrue(
            linhas.get(1).startsWith("SAQUE;40.0;")
        );
    }

    @Test
    void deveLerHistoricoExportado() {
        ContaPoupanca conta =
            new ContaPoupanca("Maria", "001");

        conta.depositar(100.0);
        conta.sacar(40.0);

        Path arquivo =
            pastaTemporaria.resolve("historico.txt");

        HistoricoArquivoService service =
            new HistoricoArquivoService();

        service.exportar(conta, arquivo);

        List<String> linhas =
            service.lerHistorico(arquivo);

        assertEquals(2, linhas.size());

        assertTrue(
            linhas.get(0).startsWith("DEPOSITO;100.0;")
        );

        assertTrue(
            linhas.get(1).startsWith("SAQUE;40.0;")
        );
    }
}
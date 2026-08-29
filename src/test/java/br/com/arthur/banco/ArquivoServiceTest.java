package br.com.arthur.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArquivoServiceTest {

    @TempDir
    Path pastaTemporaria;

    @Test
    void deveSalvarELerArquivo() throws IOException {
        ArquivoService service = new ArquivoService();

        Path arquivo =
            pastaTemporaria.resolve("teste.txt");

        service.salvarTexto(
            arquivo,
            "Sistema bancário"
        );

        assertTrue(Files.exists(arquivo));

        String conteudo =
            service.lerTexto(arquivo);

        assertEquals(
            "Sistema bancário",
            conteudo
        );
    }
}
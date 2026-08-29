package br.com.arthur.banco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArquivoService {

    public void salvarTexto(Path caminho, String conteudo)
        throws IOException {

        Files.writeString(caminho, conteudo);
    }

    public String lerTexto(Path caminho)
        throws IOException {

        return Files.readString(caminho);
    }
}
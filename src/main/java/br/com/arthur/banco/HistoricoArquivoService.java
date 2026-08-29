package br.com.arthur.banco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HistoricoArquivoService {

    public void exportar(
        ContaBancaria conta,
        Path caminho
    ) throws IOException {

        if (conta == null) {
            throw new IllegalArgumentException(
                "A conta não pode ser nula."
            );
        }

        if (caminho == null) {
            throw new IllegalArgumentException(
                "O caminho não pode ser nulo."
            );
        }

        List<String> linhas = conta.getTransacaosHistorico()
            .stream()
            .map(transacao ->
                transacao.getTipo()
                    + ";"
                    + transacao.getValor()
                    + ";"
                    + transacao.getDataHoraFormatada()
            )
            .toList();

        Files.write(caminho, linhas);
    }
}